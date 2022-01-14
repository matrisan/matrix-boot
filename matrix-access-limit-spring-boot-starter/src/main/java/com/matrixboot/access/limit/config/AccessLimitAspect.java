package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.annotation.AccessLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;

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
public class AccessLimitAspect {

    @Resource
    private ExpressionParser expressionParser;

    @Resource
    private ParameterNameDiscoverer parameterNameDiscoverer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BeanResolver beanResolver;

    @Resource
    private RedisScript<Boolean> redisScript;

    /**
     * 对方法进行计算
     *
     * @param joinPoint   方法切点
     * @param accessLimit 注解
     * @return Object
     * @throws Throwable 执行的异常
     */
    @Around("@annotation(accessLimit)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, AccessLimit accessLimit) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AccessLimitMeta meta = getIdempotentMeta(accessLimit);
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(beanResolver);
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            String paramName = parameterNames[i];
            // 参数名称和参数对象设置到表达式上下文对象里,这样才能通过 #reqVo 这样的写法来引用方法参数
            evaluationContext.setVariable(paramName, args[i]);
        }
        String spEl = meta.getValue();
        String value = (String) expressionParser.parseExpression(spEl).getValue(evaluationContext);
        assert value != null;
        Boolean aBoolean = stringRedisTemplate.execute(redisScript, Collections.singletonList(value), meta.getTimeout(), meta.getTimes());
        log.info("value: {} - {}", value, aBoolean);
        if (Boolean.TRUE.equals(aBoolean)) {
            throw new AccessLimitException("限流问题");
        }
        return joinPoint.proceed();
    }

    @Contract("_ -> new")
    private @NotNull AccessLimitMeta getIdempotentMeta(@NotNull AccessLimit accessLimit) {
        return new AccessLimitMeta(
                accessLimit.value(),
                accessLimit.reveal(),
                accessLimit.times(),
                accessLimit.timeout()

        );
    }

}
