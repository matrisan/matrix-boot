package com.matrixboot.validation.constraint;

import com.matrixboot.validation.validator.InnerIPv4Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 * <p>
 * create in 2021/11/30 3:49 下午
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InnerIPv4Validator.class)
public @interface InnerIPv4 {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
