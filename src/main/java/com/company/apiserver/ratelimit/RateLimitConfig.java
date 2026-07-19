package com.company.apiserver.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.ratelimite.RateLimiterController;
import com.company.ratelimite.TokenBucketStrategy;

/**
 * The bridge between YOUR plain-Java LLD classes and Spring.
 *
 * @Configuration = "this class builds beans by hand".
 * Each @Bean method runs once at startup; whatever it returns becomes a
 * bean (a Spring-managed singleton) that can be injected anywhere - here,
 * into RateLimitFilter's constructor.
 *
 * This is the second way to create beans (the first being annotations like
 * @Service on the class itself). You use @Bean when the class is not yours
 * to annotate - a library class, or, like here, plain design-pattern code
 * you deliberately keep framework-free.
 *
 * @Value("${ratelimit.capacity:5}") reads the property from
 * application.properties; the part after ':' is the default if missing.
 *
 * Want to try your other algorithm? Swap one line:
 *   return new RateLimiterController(
 *       new SlidingWindowLogStrategy(5, 10_000)); // 5 requests / 10s window
 */
@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimiterController rateLimiterController(
            @Value("${ratelimit.capacity:5}") double capacity,
            @Value("${ratelimit.refill-per-second:1}") long refillPerSecond) {
        return new RateLimiterController(new TokenBucketStrategy(capacity, refillPerSecond));
    }
}
