package com.matrixboot.validation.constraint;


import com.matrixboot.validation.validator.NotInnerIPv4Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * create in 2021/11/30 3:49 下午
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotInnerIPv4Validator.class)
public @interface NotInnerIPv4 {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
