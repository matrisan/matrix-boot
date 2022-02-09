package com.matrixboot.idempotent.config;

import com.matrixboot.idempotent.core.AbstractIdempotentService;
import com.matrixboot.idempotent.core.IdempotentAnnotationBeanPostProcessor;
import com.matrixboot.idempotent.core.IdempotentServiceRedisImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.StaticScriptSource;

/**
 * <p>
 * create in 2021/12/16 10:36 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@EnableAspectJAutoProxy
public class IdempotentConfig {

    /**
     * 幂等元数据信息
     *
     * @return IdempotentProperties
     */
    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    IdempotentProperties idempotentProperties() {
        return new IdempotentProperties();
    }

    /**
     * 定义一个接口用来获取 token
     *
     * @return IdempotentController
     */
    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    IdempotentController idempotentController() {
        return new IdempotentController();
    }

    /**
     * 幂等注解的核心生效类
     *
     * @return IdempotentAnnotationBeanPostProcessor
     */
    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    IdempotentAnnotationBeanPostProcessor idempotentAnnotationBeanPostProcessor() {
        return new IdempotentAnnotationBeanPostProcessor();
    }

    /**
     * 幂等接口处理类
     *
     * @return AbstractIdempotentService
     */
    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    AbstractIdempotentService idempotentService() {
        return new IdempotentServiceRedisImpl();
    }

    /**
     * Redis 脚本,主要的一些判断逻辑均在 redis 中判断
     *
     * @return RedisScript
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    RedisScript<Boolean> redisScriptIdempotentCheck() {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        String scriptStr = "if redis.call(\"EXISTS\", KEYS[1]) == 1 then\n" +
                "    redis.call(\"DEL\", KEYS[1])\n" +
                "    return 0;\n" +
                "else\n" +
                "    return 1;\n" +
                "end";
        script.setScriptSource(new StaticScriptSource(scriptStr));
        script.setResultType(Boolean.class);
        return script;
    }

    /**
     * Redis 脚本,主要的一些判断逻辑均在 redis 中判断
     *
     * @return RedisScript
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    RedisScript<Boolean> redisScriptIdempotentInsert() {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        String scriptStr = "if redis.call(\"EXISTS\", KEYS[1]) == 1 then\n" +
                "    return 0;\n" +
                "else\n" +
                "    redis.call(\"SET\",KEYS[1], ARGV[1]);\n" +
                "    return 1;\n" +
                "end";
        script.setScriptSource(new StaticScriptSource(scriptStr));
        script.setResultType(Boolean.class);
        return script;
    }

}
