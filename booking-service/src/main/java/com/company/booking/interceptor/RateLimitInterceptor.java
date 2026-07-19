package com.company.booking.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.booking.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fixed-window rate limiter, keyed per tenant: N requests per minute.
 *
 * WHY AN INTERCEPTOR (not a filter): the limit is an API-level policy and we
 * want it applied only to /api/** paths (configured in WebConfig), with easy
 * access to Spring's error-shape. Auth/infra concerns sit in filters;
 * API policies sit nicely in interceptors.
 *
 * WHY preHandle RETURNS false: returning false stops the pipeline - the
 * controller never runs. We write the 429 body ourselves because the request
 * never reaches @RestControllerAdvice (that only wraps handler execution).
 *
 * PRODUCTION NOTES (say these in the interview):
 *  - Fixed window has a burst-at-boundary flaw: 60 requests at 11:59:59 +
 *    60 at 12:00:00 = 120 in two seconds. Sliding window / token bucket fixes it.
 *  - This map is per-JVM. Behind a load balancer each node has its own counter;
 *    real systems keep the counters in Redis (e.g. Bucket4j + Redis).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int LIMIT_PER_MINUTE = 120;

    private final ObjectMapper objectMapper;

    private record Window(long minute, AtomicInteger count) {}
    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {

        String key = req.getHeader("X-Tenant-Id");
        if (key == null || key.isBlank()) key = req.getRemoteAddr();   // fallback: per-IP

        long thisMinute = System.currentTimeMillis() / 60_000;

        Window w = windows.compute(key, (k, old) ->
            (old == null || old.minute() != thisMinute)
                ? new Window(thisMinute, new AtomicInteger(0))
                : old);

        int used = w.count().incrementAndGet();
        res.setHeader("X-RateLimit-Limit", String.valueOf(LIMIT_PER_MINUTE));
        res.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, LIMIT_PER_MINUTE - used)));

        if (used > LIMIT_PER_MINUTE) {
            log.warn("Rate limit exceeded for key={} ({}/{})", key, used, LIMIT_PER_MINUTE);
            res.setStatus(429);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(res.getWriter(),
                ErrorResponse.of(429, "TooManyRequests",
                    "Rate limit of " + LIMIT_PER_MINUTE + "/min exceeded", req.getRequestURI()));
            return false;                       // short-circuit: controller never runs
        }
        return true;
    }
}
