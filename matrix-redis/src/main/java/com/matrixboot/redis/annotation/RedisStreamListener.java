package com.matrixboot.redis.annotation;

import org.springframework.messaging.handler.annotation.MessageMapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * create in 2021/12/3 5:57 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MessageMapping
@Documented
@Repeatable(RedisStreamListeners.class)
public @interface RedisStreamListener {

    String[] topics() default {};

    String errorHandler() default "";

    String groupId() default "";

    String clientIdPrefix() default "";

}
