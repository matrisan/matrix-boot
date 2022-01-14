package com.matrixboot.access.limit.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scripting.support.StaticScriptSource;

/**
 * 请求速率的一些限制
 * <p>
 * create in 2022/1/14 5:08 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@EnableAspectJAutoProxy
public class AccessLimitConfig {


    /**
     * Redis 脚本,主要的一些判断逻辑均在 redis 中判断
     *
     * @return RedisScript
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    RedisScript<Boolean> redisScript() {
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
    public AccessLimitAnnotationBeanPostProcessor accessLimitAnnotationBeanPostProcessor() {
        return new AccessLimitAnnotationBeanPostProcessor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    AccessLimitRedisServiceImpl accessLimitRedisService() {
        return new AccessLimitRedisServiceImpl();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    AccessLimitProperties accessLimitProperties() {
        return new AccessLimitProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    ExpressionParser expressionParser() {
        return new SpelExpressionParser();
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    ParameterNameDiscoverer parameterNameDiscoverer() {
        return new DefaultParameterNameDiscoverer();
    }

    @Bean
    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    BeanResolver beanResolver(BeanFactory beanFactory) {
        return new BeanFactoryResolver(beanFactory);
    }


}
