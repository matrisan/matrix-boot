package com.matrixboot.semaphore.config;

import com.matrixboot.semaphore.annotation.Semaphore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
public class SemaphoreAspect {

    private final ISemaphore semaphore;

    private final SemaphoreProperties properties;

    private SemaphoreMeta meta;

    public SemaphoreAspect(ISemaphore semaphore, SemaphoreProperties properties) {
        this.semaphore = semaphore;
        this.properties = properties;
    }

    /**
     * AOP 切面
     *
     * @param joinPoint ProceedingJoinPoint
     * @param semaphore Brake
     * @return Object
     * @throws Throwable 异常信息
     */
    @Around("@annotation(semaphore)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, Semaphore semaphore) throws Throwable {
        SemaphoreMeta semaphoreMeta = getSemaphoreMeta(semaphore);
        boolean acquire = this.semaphore.tryAcquire(semaphoreMeta);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } finally {
            this.semaphore.release(semaphoreMeta.getKey());
        }
        return proceed;
    }

    private SemaphoreMeta getSemaphoreMeta(Semaphore semaphore) {
        if (Objects.isNull(meta)) {
            meta = new SemaphoreMeta(semaphore, properties);
        }
        return meta;
    }

}
