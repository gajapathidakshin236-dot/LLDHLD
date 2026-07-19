package com.company.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @EnableAsync activates the proxy machinery for @Async - same story as
 * @EnableCaching for @Cacheable. Forget it and @Async methods silently run
 * synchronously on the caller thread.
 *
 * ALWAYS define your own executor. The default SimpleAsyncTaskExecutor spawns
 * a brand-new thread per task - no pooling, no upper bound. Under load that's
 * unbounded thread creation, which is the same class of bug as the missing
 * max-page-size: an unbounded resource.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "notificationExecutor")
    public ThreadPoolTaskExecutor notificationExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(2);                 // always-alive threads
        ex.setMaxPoolSize(8);                  // burst ceiling
        ex.setQueueCapacity(100);              // bounded queue - back-pressure, not OOM
        ex.setThreadNamePrefix("notif-");
        // When queue is full: run on the CALLER thread instead of dropping.
        // Slows the producer down = natural back-pressure.
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        ex.initialize();
        return ex;
    }
}
