package com.matrixboot.access.limit.config;

import com.matrixboot.access.limit.dto.AccessLimitResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.env.Environment;
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

    /**
     * SPEL 表达式解析
     *
     * @param expressionParser expressionParser
     */
    void setExpressionParser(ExpressionParser expressionParser);

    /**
     * 参数名称解析
     *
     * @param parameterNameDiscoverer parameterNameDiscoverer
     */
    void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer);

    /**
     * redis stringRedisTemplate
     *
     * @param stringRedisTemplate stringRedisTemplate
     */
    void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate);

    /**
     * spring 容器 bean 解析器
     *
     * @param beanResolver beanResolver
     */
    void setBeanResolver(BeanResolver beanResolver);

    /**
     * 设置 lua 脚本
     *
     * @param redisScript redisScript
     */
    void setRedisScript(RedisScript<Boolean> redisScript);

    /**
     * 设置环境变量
     *
     * @param environment 环境变量
     */
    void setEnvironment(Environment environment);

    /**
     * 真正执行 check 的方法
     *
     * @param method 待执行的方法
     * @param args   方法的参数
     * @return AccessLimitResult
     */
    AccessLimitResult doCheck(Method method, Object[] args);

    /**
     * 回调函数名称
     *
     * @return String
     */
    String getReveal();

    /**
     * 设置参数
     *
     * @param properties properties
     */
    void setAccessLimitProperties(AccessLimitProperties properties);
}
