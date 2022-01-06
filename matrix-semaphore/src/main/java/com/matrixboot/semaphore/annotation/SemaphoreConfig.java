package com.matrixboot.semaphore.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>
 * create in 2021/12/23 4:02 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@EnableAspectJAutoProxy
public class SemaphoreConfig {

    @Bean
    public SemaphoreProperties brakeProperties(){
        return new SemaphoreProperties();
    }

    @Bean
    public SemaphoreAspect doorkeeperAspect(ISemaphore iSemaphore, SemaphoreProperties properties) {
        return new SemaphoreAspect(iSemaphore, properties);
    }

    @Bean
    public ISemaphore iSemaphore(StringRedisTemplate stringRedisTemplate) {
        return new SemaphoreRedisImpl(stringRedisTemplate);
    }

}
