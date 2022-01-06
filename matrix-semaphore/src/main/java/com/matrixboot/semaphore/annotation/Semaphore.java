package com.matrixboot.semaphore.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * create in 2021/12/23 3:48 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Semaphore {

    /**
     * 这个值必填,不同的接口填写的不一样
     *
     * @return String
     */
    String value();

    long timeout() default 1;

    TimeUnit unit() default TimeUnit.SECONDS;


}
