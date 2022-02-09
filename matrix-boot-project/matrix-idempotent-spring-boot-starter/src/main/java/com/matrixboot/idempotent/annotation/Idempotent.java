package com.matrixboot.idempotent.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * create in 2021/12/16 9:42 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Idempotent {

    /**
     * 当前方法的一个标识
     *
     * @return STRING
     */
    String value();

    /**
     * 当发生幂等问题时,回调的方法名称
     *
     * @return STRING
     */
    String recover() default "";

    /**
     * 提示语
     *
     * @return STRING
     */
    String message() default "请求未携带 token 或 token 已经被消费!";

}
