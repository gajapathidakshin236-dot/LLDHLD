package com.company.apiserver.ratelimit;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.company.ratelimite.RateLimiterController;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Your RateLimiterController's Javadoc said:
 *   "In a real system this would be a servlet filter returning HTTP 429
 *    on rejection."
 * This class IS that real system.
 *
 * A servlet FILTER sits in front of every controller: each incoming request
 * passes through doFilter() BEFORE routing. The filter chooses: pass it on
 * (chain.doFilter) or answer immediately itself.
 *
 * @Component registers it as a bean; Spring Boot auto-applies any Filter
 * bean to all requests. We only guard paths starting with /api/ so the
 * H2 console stays freely accessible.
 *
 * Client identity = the caller's IP address. From your own machine that is
 * always 127.0.0.1, so YOU are one bucket - which makes the limiter easy
 * to see: hammer /api/ping and watch 200s turn into 429s.
 */
@Component
public class RateLimitFilter implements Filter {

    private final RateLimiterController rateLimiter;

    /** Injected: the bean built in RateLimitConfig from YOUR classes. */
    public RateLimitFilter(RateLimiterController rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Only rate-limit the API itself
        if (!req.getRequestURI().startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        String clientId = req.getRemoteAddr(); // IP address as the bucket key

        if (rateLimiter.isAllowed(clientId)) {
            chain.doFilter(request, response); // token consumed -> proceed
            return;
        }

        // Bucket empty -> block the request right here. 429 = Too Many Requests.
        res.setStatus(429);
        res.setContentType("application/json");
        res.getWriter().write(
            "{\"status\":429,\"error\":\"Too Many Requests\"," +
            "\"message\":\"Rate limit exceeded (token bucket empty). Wait a second and retry.\"}");
    }
}
