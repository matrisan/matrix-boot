package com.matrixboot.redis.annotation;

import com.matrixboot.redis.config.consumer.RedisStreamListenerConfig;
import com.matrixboot.redis.config.producer.RedisStreamClientsRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * create in 2021/12/7 9:27 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({RedisStreamClientsRegistrar.class, RedisStreamListenerConfig.class})
public @interface EnableRedisStreamClients {
}
