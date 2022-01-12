package com.matrixboot.idempotent.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.OrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * create in 2021/12/16 9:58 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
@Aspect
@Order(2)
@Component
public class IdempotentAspect implements InitializingBean {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private IdempotentProperties properties;

    @Resource
    private RedisScript<Boolean> redisScript;

    @Resource
    private List<IIdempotentHook> hooks;

    /**
     * 对方法进行计算
     *
     * @param joinPoint  方法切点
     * @param idempotent 注解
     * @return Object
     * @throws Throwable 执行的异常
     */
    @Around("@annotation(idempotent)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        String redisKey = IdempotentCommon.getRedisKey(properties, getToken());
        if (Boolean.TRUE.equals(stringRedisTemplate.execute(redisScript, Collections.singletonList(redisKey)))) {
            return joinPoint.proceed();
        }
        hooks.forEach(IIdempotentHook::invoke);
        throw new IdempotentException("幂等问题");
    }

    private String getToken() {
        return httpServletRequest.getHeader("idempotent");
    }

    @Override
    public void afterPropertiesSet() {
        OrderComparator.sort(hooks);
    }
}
