package com.matrixboot.access.limit.annotation;

import com.matrixboot.access.limit.exception.AccessLimitException;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * create in 2022/1/18 2:40 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AccessLimits {

    AccessLimit[] value();

    /**
     * 发生请求限制问题时,回调的函数,
     * 如果不写则抛出异常,需要玩家自己去捕获这个异常 {@link AccessLimitException}
     *
     * @return String
     */
    String recover() default "";
}
