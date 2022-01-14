package com.matrixboot.semaphore.config;

import com.matrixboot.semaphore.annotation.Semaphore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>
 * create in 2021/12/23 3:50 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
@Aspect
@Order(2)
@Component
@AllArgsConstructor
public class SemaphoreAspect {

    private final ISemaphore semaphore;

    private final SemaphoreProperties properties;

    /**
     * AOP 切面
     *
     * @param joinPoint ProceedingJoinPoint
     * @param semaphore     Brake
     * @return Object
     * @throws Throwable 异常信息
     */
    @Around("@annotation(semaphore)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, Semaphore semaphore) throws Throwable {
        SemaphoreMeta meta = new SemaphoreMeta(semaphore, properties);
        this.semaphore.tryAcquire(meta);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } finally {
            this.semaphore.release(meta.getKey());
        }
        return proceed;
    }

}
