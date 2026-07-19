package com.company.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the whole application. In IntelliJ, click the green
 * arrow next to main() to start the server.
 *
 * @SpringBootApplication is three annotations in one:
 *   1. @Configuration        - this class can define setup/wiring
 *   2. @EnableAutoConfiguration - Spring Boot looks at the jars on the
 *      classpath and configures everything automatically (sees H2 -> sets up
 *      a datasource; sees spring-web -> starts embedded Tomcat; etc.)
 *   3. @ComponentScan        - scan THIS package (com.company.apiserver) and
 *      all sub-packages for classes marked @RestController/@Service/etc.
 *      and manage them.
 *
 * Note: your design-pattern classes in com.company.ratelimite are NOT
 * annotated and NOT in this package tree - they stay plain Java, exactly as
 * you wrote them. They enter the Spring world in one place only:
 * ratelimit/RateLimitConfig.java creates them as beans.
 *
 * SpringApplication.run(...) starts the whole machine, including the
 * embedded Tomcat web server listening on http://localhost:8080
 */
@SpringBootApplication
public class ApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }
}
