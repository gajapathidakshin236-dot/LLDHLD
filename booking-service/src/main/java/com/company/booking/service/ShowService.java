package com.company.booking.service;

import com.company.booking.entity.Show;
import com.company.booking.exception.BookingNotFoundException;
import com.company.booking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Read-heavy service. Show data barely changes between requests, so cache it.
 * <p>
 * How {@code @Cacheable} works:
 * <ol>
 *   <li>Spring wraps this bean in a proxy at startup.</li>
 *   <li>On method entry, the proxy computes the cache key from parameters
 *       (default = {@code SimpleKeyGenerator} concatenating args).</li>
 *   <li>If the key hits in cache "shows" -> return cached value, method body NEVER runs.</li>
 *   <li>If miss -> body runs, result is put under the key, then returned.</li>
 * </ol>
 * <p>
 * Two footguns to know for the interview:
 * <ul>
 *   <li><b>Self-invocation bypasses the proxy.</b> Calling {@code this.getShow(id)}
 *       from another method in the same class skips the cache entirely because
 *       you're not going through the proxy. Always call across beans.</li>
 *   <li><b>Cache is scoped per JVM by default.</b> Caffeine is in-process.
 *       In a cluster you either accept per-node caches or move to Redis.</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepo;

    /**
     * Key = (tenantId, showId). Two tenants asking for the same showId get
     * different cache entries - critical for multi-tenant correctness.
     */
    /**
     * AUDIT FIX #2 - the key used to be T(java.util.Objects).hash(#tenantId,#showId).
     * A hash is NOT unique: two different (tenant, show) pairs can collide on the
     * same int, and a collision here means tenant A gets tenant B's cached show -
     * a cross-tenant data LEAK. Cache keys must be injective (collision-free):
     * string concatenation "42:7" is unique per pair. Lesson: never build cache
     * keys from hashes.
     */
    @Cacheable(value = "shows", key = "#tenantId + ':' + #showId")
    @Transactional(readOnly = true)
    public Show getShow(Long tenantId, Long showId) {
        log.info("DB HIT (cache miss) for show tenant={} id={}", tenantId, showId);
        return showRepo.findByIdAndTenantId(showId, tenantId)
            .orElseThrow(() -> new BookingNotFoundException("Show " + showId + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Show> listShows(Long tenantId) {
        return showRepo.findAllByTenantId(tenantId);
    }

    /**
     * If a show mutates (title, price, cancelled), evict it so the next read reloads.
     * {@code allEntries = true} would nuke the whole cache; here we're surgical.
     */
    /**
     * PUT semantics - full replacement of every mutable field.
     * Cache entry evicted so the next read reloads fresh data.
     * If two admins PUT concurrently, @Version on Show turns the slower write
     * into ObjectOptimisticLockingFailureException -> 409 via the handler.
     */
    @CacheEvict(value = "shows", key = "#tenantId + ':' + #showId")
    @Transactional
    public Show updateShow(Long tenantId, Long showId,
                           com.company.booking.dto.request.UpdateShowRequest req) {
        Show show = showRepo.findByIdAndTenantId(showId, tenantId)
            .orElseThrow(() -> new BookingNotFoundException("Show " + showId + " not found"));
        show.setTitle(req.title());
        show.setVenue(req.venue());
        show.setTotalSeats(req.totalSeats());
        show.setPriceCents(req.priceCents());
        show.setStartsAt(req.startsAt());
        return show;                    // dirty-checked, flushed at commit
    }

    @CacheEvict(value = "shows", key = "#tenantId + ':' + #showId")
    @Transactional
    public Show updateTitle(Long tenantId, Long showId, String newTitle) {
        Show show = showRepo.findByIdAndTenantId(showId, tenantId)
            .orElseThrow(() -> new BookingNotFoundException("Show " + showId + " not found"));
        show.setTitle(newTitle);
        return showRepo.save(show);
    }
}
