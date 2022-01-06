package com.matrixboot.brake.annotation;

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
public class BrakeConfig {

    @Bean
    public BrakeProperties brakeProperties(){
        return new BrakeProperties();
    }

    @Bean
    public BrakeAspect doorkeeperAspect(ISemaphore iSemaphore, BrakeProperties properties) {
        return new BrakeAspect(iSemaphore, properties);
    }

    @Bean
    public ISemaphore iSemaphore(StringRedisTemplate stringRedisTemplate) {
        return new SemaphoreRedisImpl(stringRedisTemplate);
    }

}
