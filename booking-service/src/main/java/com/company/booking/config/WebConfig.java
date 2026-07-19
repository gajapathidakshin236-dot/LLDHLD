package com.company.booking.config;

import com.company.booking.interceptor.LoggingInterceptor;
import com.company.booking.interceptor.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer = the hook Spring MVC gives you to customise its pipeline
 * WITHOUT replacing auto-configuration. Interceptors are NOT auto-registered
 * by @Component alone - you must add them here. (Common interview trap:
 * "my interceptor never fires" -> it was never registered.)
 *
 * Registration order = execution order for preHandle;
 * afterCompletion unwinds in REVERSE order (stack semantics).
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1st: rate limit - reject over-limit traffic before we waste time logging it
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**");

        // 2nd: request logging with handler-method detail + timing
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/health");
    }

    /**
     * CORS - browser protection, enforced by the BROWSER not the server.
     * A JS app on localhost:3000 calling this API needs the server to declare
     * that origin allowed, otherwise the browser blocks the response.
     * Non-browser clients (curl, Postman, other services) ignore CORS entirely.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                .allowedHeaders("Content-Type", "X-Tenant-Id", "X-Correlation-Id")
                .exposedHeaders("X-Correlation-Id", "X-RateLimit-Remaining")
                .maxAge(3600);
    }
}
