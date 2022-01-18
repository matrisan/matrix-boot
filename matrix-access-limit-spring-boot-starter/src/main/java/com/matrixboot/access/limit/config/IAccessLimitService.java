package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.dto.AccessLimitResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Method;

/**
 * <p>
 * create in 2022/1/14 4:58 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
public interface IAccessLimitService extends InitializingBean {

    void setExpressionParser(ExpressionParser expressionParser);

    void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer);

    void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate);

    void setBeanResolver(BeanResolver beanResolver);

    void setRedisScript(RedisScript<Boolean> redisScript);

    /**
     * 真正执行 check 的方法
     *
     * @param method 待执行的方法
     * @param args   方法的参数
     * @return AccessLimitResult
     */
    AccessLimitResult doCheck(Method method, Object[] args);

    String getReveal();

    void setAccessLimitProperties(AccessLimitProperties properties);
}
