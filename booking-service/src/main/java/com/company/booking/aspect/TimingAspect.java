package com.company.booking.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * WHAT AOP IS (30-second version):
 * Aspect-Oriented Programming = extracting cross-cutting concerns (timing,
 * logging, security, transactions) out of business code. Spring implements it
 * with PROXIES: your bean gets wrapped in a generated object that runs the
 * aspect code around every matching method call.
 *
 * @Transactional and @Cacheable are literally built on this same machinery -
 * which is WHY self-invocation breaks them: this.method() skips the proxy.
 * Same rule applies to this aspect.
 *
 * POINTCUT SYNTAX:
 *   "within(com.company.booking.service..*)" = every method of every class
 *   under the service package. The @Around advice receives the call as a
 *   ProceedingJoinPoint - we control if/when the real method runs.
 *
 * Interceptor vs Aspect: the interceptor timed the whole HTTP request;
 * this times each SERVICE method. Subtracting the two tells you how much
 * time went to serialization/filters vs business logic.
 */
@Slf4j
@Aspect
@Component
public class TimingAspect {

    @Around("within(com.company.booking.service..*)")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();                      // invoke the real method
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            // Only log slow calls to keep noise down; drop the guard to see all
            if (ms > 5) {
                log.debug("[timing] {}.{} took {}ms",
                    pjp.getSignature().getDeclaringType().getSimpleName(),
                    pjp.getSignature().getName(), ms);
            }
        }
    }
}
