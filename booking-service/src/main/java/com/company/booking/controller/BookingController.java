package com.company.booking.controller;

import com.company.booking.context.TenantContext;
import com.company.booking.dto.request.CreateBookingRequest;
import com.company.booking.dto.response.BookingResponse;
import com.company.booking.dto.response.CursorPage;
import com.company.booking.dto.response.PagedResponse;
import com.company.booking.entity.Booking;
import com.company.booking.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST entry points.
 * <p>
 * Three endpoints:
 * <ul>
 *   <li>{@code POST /api/v1/bookings}          create</li>
 *   <li>{@code GET  /api/v1/bookings}          offset pagination (admin table view)</li>
 *   <li>{@code GET  /api/v1/bookings/feed}     keyset pagination (feed / deep pages)</li>
 * </ul>
 * <p>
 * Tenant is NOT a path variable or query param - it comes from the
 * {@link TenantContext} the filter set. That way no controller can accidentally
 * forget to scope by tenant.
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody CreateBookingRequest req) {
        Booking created = bookingService.createBooking(
            TenantContext.get(), req.showId(), req.userId(), req.seatNo());

        BookingResponse body = BookingResponse.from(created);
        return ResponseEntity
            .created(URI.create("/api/v1/bookings/" + created.getId()))
            .body(body);
    }

    // -------------------------------------------------------------------------
    // GET ONE - GET /api/v1/bookings/{id}
    // Wrong tenant or missing id -> 404 (never 403; don't reveal existence).
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(
            BookingResponse.from(bookingService.getBooking(TenantContext.get(), id)));
    }

    // -------------------------------------------------------------------------
    // DELETE - DELETE /api/v1/bookings/{id}
    // Soft delete: flips status to CANCELLED and returns the updated resource.
    // 200 + body (not 204) so the client sees the resulting state. Idempotent:
    // deleting twice returns the same CANCELLED booking.
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<BookingResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(
            BookingResponse.from(bookingService.cancelBooking(TenantContext.get(), id)));
    }

    // -------------------------------------------------------------------------
    // OFFSET LIST - GET /api/v1/bookings?page=0&size=20&sort=createdAt,desc
    // Pageable is populated by PageableHandlerMethodArgumentResolver.
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<PagedResponse<BookingResponse>> list(
            @PageableDefault(size = 20)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Booking> page = bookingService.listBookings(TenantContext.get(), pageable);
        // Page.map() transforms content while preserving paging metadata
        return ResponseEntity.ok(PagedResponse.from(page.map(BookingResponse::from)));
    }

    // -------------------------------------------------------------------------
    // KEYSET FEED - GET /api/v1/bookings/feed?size=20&cursor=<base64>
    // No `page` param BY DESIGN - you cannot jump to page 500 with keyset.
    // -------------------------------------------------------------------------
    @GetMapping("/feed")
    public ResponseEntity<CursorPage<BookingResponse>> feed(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        CursorPage<Booking> page = bookingService.listByCursor(TenantContext.get(), cursor, size);

        List<BookingResponse> mapped = page.content().stream()
            .map(BookingResponse::from)
            .toList();

        return ResponseEntity.ok(CursorPage.of(mapped, page.nextCursor(), page.hasMore()));
    }

    // -------------------------------------------------------------------------
    // SLICE - GET /api/v1/bookings/by-status?status=CONFIRMED&page=0&size=10
    // One query (size+1 probe), no COUNT. Response has hasNext but NO totals.
    // Bad status string ("?status=WRONG") -> MethodArgumentTypeMismatchException
    // -> 400 via GlobalExceptionHandler (Spring converts String->enum for you).
    // -------------------------------------------------------------------------
    @GetMapping("/by-status")
    public ResponseEntity<com.company.booking.dto.response.SliceResponse<BookingResponse>> byStatus(
            @RequestParam com.company.booking.entity.BookingStatus status,
            @PageableDefault(size = 20)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        var slice = bookingService.listByStatus(TenantContext.get(), status, pageable);
        return ResponseEntity.ok(
            com.company.booking.dto.response.SliceResponse.from(slice.map(BookingResponse::from)));
    }

    // -------------------------------------------------------------------------
    // SEARCH - GET /api/v1/bookings/search?showId=1&page=0&size=10
    // showId is OPTIONAL: the JPQL handles null with (:showId IS NULL OR ...).
    // Demonstrates Page from a custom @Query - which REQUIRES an explicit
    // countQuery (Spring can't derive a count from hand-written JPQL reliably).
    // -------------------------------------------------------------------------
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<BookingResponse>> search(
            @RequestParam(required = false) Long showId,
            @PageableDefault(size = 20)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Booking> page = bookingService.searchBookings(TenantContext.get(), showId, pageable);
        return ResponseEntity.ok(PagedResponse.from(page.map(BookingResponse::from)));
    }

    // -------------------------------------------------------------------------
    // Health-check style endpoint for the interview: prove the whole flow works
    // -------------------------------------------------------------------------
    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public long count() {
        return bookingService.listBookings(TenantContext.get(),
                Pageable.ofSize(1)).getTotalElements();
    }
}
