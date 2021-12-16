package com.matrixboot.idempotent.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
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
public class IdempotentAspect {

    @Resource
    private ExpressionParser expressionParser;

    @Resource
    private ParameterNameDiscoverer parameterNameDiscoverer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BeanResolver beanResolver;

    @Pointcut("@annotation(com.matrixboot.idempotent.annotation.Idempotent) && @annotation(idempotent)")
    public void lockPointCut(Idempotent idempotent) {
    }

    @Around("lockPointCut(idempotent)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(beanResolver);
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            String paramName = parameterNames[i];
            // 参数名称和参数对象设置到表达式上下文对象里,这样才能通过 #reqVo 这样的写法来引用方法参数
            evaluationContext.setVariable(paramName, args[i]);
        }
        String spEl = idempotent.value();
        String value = (String) expressionParser.parseExpression(spEl).getValue(evaluationContext);
        assert value != null;
        IdempotentMeta meta = getIdempotentMeta(idempotent);
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(meta.getValue(), "", meta.getTimeout(), meta.getUnit());
        log.info("value: {} - {}", value, aBoolean);
        if (Boolean.FALSE.equals(aBoolean)) {
            throw new IdempotentException("幂等问题");
        }
        return joinPoint.proceed();
    }

    @Contract("_ -> new")
    private @NotNull IdempotentMeta getIdempotentMeta(@NotNull Idempotent idempotent) {
        return new IdempotentMeta(idempotent.value(), idempotent.timeout(), idempotent.unit());
    }

}
