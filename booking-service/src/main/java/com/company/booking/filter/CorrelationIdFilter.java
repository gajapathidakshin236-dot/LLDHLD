package com.company.booking.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * WHY THIS IS A FILTER AND NOT AN INTERCEPTOR:
 * Filters run before the DispatcherServlet - they see EVERY request, including
 * ones that never reach a controller (404s, static resources, errors thrown by
 * other filters). A correlation id must exist for all of those too, so it
 * belongs at the outermost layer.
 *
 * WHAT MDC IS:
 * MDC = Mapped Diagnostic Context, a per-thread map inside SLF4J/Logback.
 * Anything you put in it can be printed by the log pattern (%X{cid}).
 * Set it once at request entry -> every log line of this request carries the id
 * without any code passing it around.
 *
 * @Order(HIGHEST_PRECEDENCE) makes this the FIRST filter, so even
 * TenantFilter's own log lines carry the cid.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "cid";

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        // Honour an id sent by an upstream service (API gateway, another microservice),
        // otherwise mint a fresh one. That's how one user action is traced ACROSS services.
        String cid = req.getHeader(HEADER);
        if (cid == null || cid.isBlank()) {
            cid = UUID.randomUUID().toString().substring(0, 8);
        }

        MDC.put(MDC_KEY, cid);
        res.setHeader(HEADER, cid);       // echo back so the client can report it

        try {
            chain.doFilter(req, res);
        } finally {
            MDC.remove(MDC_KEY);          // same rule as TenantContext: pooled threads
        }
    }
}
