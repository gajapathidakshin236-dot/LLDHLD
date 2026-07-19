package com.company.booking.filter;

import com.company.booking.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads {@code X-Tenant-Id} header on every request and pins it to the thread
 * via {@link TenantContext}. The {@code finally} block is non-negotiable -
 * without it Tomcat's thread pool would carry the previous request's tenant
 * to the next request that reuses the thread.
 * <p>
 * In a real app this is where auth (JWT / OAuth2 resource server) would resolve
 * the tenant from a signed claim rather than trusting a header. Header is fine
 * for the demo.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Tenant-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        // Skip infra endpoints that shouldn't require a tenant
        String path = req.getRequestURI();
        if (path.startsWith("/actuator") || path.startsWith("/h2-console")) {
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader(HEADER);
        if (header == null || header.isBlank()) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing " + HEADER + " header");
            return;
        }

        try {
            TenantContext.set(Long.parseLong(header));
            chain.doFilter(req, res);
        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, HEADER + " must be numeric");
        } finally {
            TenantContext.clear();          // MUST run - see class Javadoc
        }
    }
}
