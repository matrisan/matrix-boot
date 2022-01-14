package com.matrixboot.redis.config.consumer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * <p>
 * create in 2022/1/13 3:29 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Configuration
public class RedisStreamListenerConfig {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public MethodRedisStreamAdvisor methodRedisStreamAdvisor(RedisStreamInterceptor interceptor, RedisStreamOperationSource operationSource) {
        MethodRedisStreamAdvisor advisor = new MethodRedisStreamAdvisor();
        advisor.setAdvice(interceptor);
        advisor.setRedisStreamOperationSource(operationSource);
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RedisStreamOperationSource cacheRedisStreamOperationSource() {
        return new RedisStreamOperationSource();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RedisStreamInterceptor redisStreamInterceptor() {
        return new RedisStreamInterceptor();
    }

}
