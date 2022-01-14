package com.matrixboot.access.limit.config2;

import com.matrixboot.access.limit.config.AccessLimitProperties;
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
 * TODO
 * <p>
 * create in 2022/1/14 5:08 PM
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
    public AccessLimitAnnotationBeanPostProcessor accessLimitAnnotationBeanPostProcessor() {
        return new AccessLimitAnnotationBeanPostProcessor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    AccessLimitRedisService accessLimitRedisService(){
        return new AccessLimitRedisService();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public AccessLimitProperties accessLimitProperties(){
        return new AccessLimitProperties();
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


}
