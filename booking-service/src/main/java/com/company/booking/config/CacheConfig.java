package com.company.booking.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Programmatic cache config so you can talk through it in an interview,
 * even though {@code spring.cache.caffeine.spec} in {@code application.yml}
 * would do the same thing.
 * <p>
 * Caffeine is in-process. In a cluster each node has its own cache; if you
 * need cross-node consistency, swap this bean for Redis (spring-boot-starter-data-redis).
 */
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("shows");
        manager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1_000)
            .expireAfterWrite(Duration.ofMinutes(5))
            .recordStats());       // exposes hit/miss metrics via /actuator/caches
        return manager;
    }
}
