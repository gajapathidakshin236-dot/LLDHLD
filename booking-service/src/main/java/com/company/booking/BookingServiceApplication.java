package com.company.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Entry point.
 * <p>
 * {@code @SpringBootApplication} bundles:
 *   - {@code @Configuration}       (this class defines beans)
 *   - {@code @EnableAutoConfiguration} (Spring Boot wires starters based on classpath)
 *   - {@code @ComponentScan}       (scans com.company.booking and sub-packages)
 * <p>
 * {@code @EnableCaching} activates the proxy that intercepts {@code @Cacheable}
 * methods. Without it, {@code @Cacheable} is silently ignored - a common bug.
 */
@SpringBootApplication
@EnableCaching
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }
}
