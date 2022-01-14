package com.matrixboot.access.limit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求速率限制注解
 * <p>
 * create in 2022/1/6 5:18 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AccessLimit {

    /**
     * 需要提取的字段
     *
     * @return String
     */
    String value();

    /**
     * 发生请求限制问题时,回调的函数,
     * 如果不写则抛出异常,需要玩家自己去捕获这个异常 {@link com.matrixboot.access.limit.config.AccessLimitException}
     *
     * @return String
     */
    String reveal() default "";

    /**
     * 请求的次数限制, 默认是 1 分钟三次
     *
     * @return int
     */
    int times() default 3;

    /**
     * 时长,单位是秒
     *
     * @return int
     */
    int timeout() default 60;

}
