package com.matrixboot.idempotent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * <p>
 * create in 2021/12/16 10:36 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@EnableAspectJAutoProxy
public class IdempotentConfig {

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public IdempotentProperties idempotentProperties() {
        return new IdempotentProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

    @Value("classpath:script/idempotent.lua")
    private Resource resource;

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(resource));
        script.setResultType(Boolean.class);
        return script;
    }

}
