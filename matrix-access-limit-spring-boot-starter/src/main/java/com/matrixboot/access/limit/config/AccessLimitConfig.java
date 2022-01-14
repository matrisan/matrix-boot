package com.matrixboot.access.limit.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scripting.support.StaticScriptSource;

import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * create in 2021/12/16 10:36 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@EnableAspectJAutoProxy
public class AccessLimitConfig {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        String scriptStr = "local threshold = tonumber(ARGV[2]);\n" +
                "local exe = redis.call(\"SETNX\", KEYS[1], 1);\n" +
                "if exe == 1 then\n" +
                "    redis.call(\"EXPIRE\", KEYS[1], ARGV[1]);\n" +
                "    return 0;\n" +
                "else\n" +
                "    local redisData = redis.call(\"INCR\", KEYS[1])\n" +
                "    return redisData > threshold;\n" +
                "end\n";
        script.setScriptSource(new StaticScriptSource(scriptStr));
        script.setResultType(Boolean.class);
        return script;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public AccessLimitAspect idempotentAspect() {
        return new AccessLimitAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ExpressionParser expressionParser() {
        return new SpelExpressionParser();
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ParameterNameDiscoverer parameterNameDiscoverer() {
        return new DefaultParameterNameDiscoverer();
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanResolver beanResolver(BeanFactory beanFactory) {
        return new BeanFactoryResolver(beanFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public StringRedisConnection getStringRedisConnection(@NotNull StringRedisTemplate stringRedisTemplate) {
        final AtomicReference<StringRedisConnection> redisConnection = new AtomicReference<>();
        stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
            redisConnection.set((StringRedisConnection) connection);
            return null;
        });
        return redisConnection.get();
    }

}
