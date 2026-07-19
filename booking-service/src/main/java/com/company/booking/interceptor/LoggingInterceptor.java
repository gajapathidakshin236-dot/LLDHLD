package com.company.booking.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * WHAT AN INTERCEPTOR IS:
 * A hook inside Spring MVC that runs AFTER the DispatcherServlet has mapped
 * the request to a controller method, but around its invocation. Compare:
 *
 *   Filter        - servlet container level, BEFORE Spring MVC. Sees raw
 *                   request/response. Doesn't know which controller will run.
 *   Interceptor   - Spring MVC level. Knows the exact HandlerMethod (controller
 *                   + method). Can't see anything that bypasses the DispatcherServlet.
 *   Aspect (AOP)  - bean-proxy level. Wraps ANY bean method (service, repo),
 *                   not just web requests.
 *
 * Request order:
 *   Filters -> DispatcherServlet -> Interceptor.preHandle -> ArgumentResolvers
 *   -> Controller -> Interceptor.postHandle -> (view render) ->
 *   Interceptor.afterCompletion -> filters unwind
 *
 * THE THREE CALLBACKS:
 *   preHandle       before controller. Return false = stop the request here.
 *   postHandle      after controller returns NORMALLY. Skipped on exception.
 *   afterCompletion ALWAYS runs (finally-semantics) - timing/cleanup goes here.
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_ATTR = "req.start.nanos";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        req.setAttribute(START_ATTR, System.nanoTime());

        // `handler` is the actual controller method - something a Filter can never see
        if (handler instanceof HandlerMethod hm) {
            log.debug("-> {} {} handled by {}#{}",
                req.getMethod(), req.getRequestURI(),
                hm.getBeanType().getSimpleName(), hm.getMethod().getName());
        }
        return true;   // false would short-circuit the request
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res,
                                Object handler, Exception ex) {
        Object start = req.getAttribute(START_ATTR);
        long ms = start == null ? -1 : (System.nanoTime() - (Long) start) / 1_000_000;

        if (ex != null) {
            log.warn("<- {} {} status={} took={}ms EXCEPTION={}",
                req.getMethod(), req.getRequestURI(), res.getStatus(), ms, ex.toString());
        } else {
            log.info("<- {} {} status={} took={}ms",
                req.getMethod(), req.getRequestURI(), res.getStatus(), ms);
        }
    }
}
