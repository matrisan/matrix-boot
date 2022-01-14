package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.exception.AccessLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 * create in 2022/1/14 4:59 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
public class AccessLimitRedisServiceImpl implements IAccessLimitService, InitializingBean {

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

    private StandardEvaluationContext evaluationContext;

    @Override
    public void doCheck(Method method, Object[] args, AccessLimitMeta meta) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            String paramName = parameterNames[i];
            // 参数名称和参数对象设置到表达式上下文对象里,这样才能通过 #reqVo 这样的写法来引用方法参数
            evaluationContext.setVariable(paramName, args[i]);
        }
        String spEl = meta.getValue();
        Object value = expressionParser.parseExpression(spEl).getValue(evaluationContext);
        assert value != null;
        Boolean aBoolean = stringRedisTemplate.execute(redisScript, Collections.singletonList(value.toString()), meta.getTimeout(), meta.getTimes());
        log.info("value: {} - {}", value, aBoolean);
        if (Boolean.TRUE.equals(aBoolean)) {
            throw new AccessLimitException("限流问题");
        }
    }

    @Override
    public void afterPropertiesSet() {
        evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(beanResolver);
    }
}
