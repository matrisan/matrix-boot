package com.matrixboot.validation.constraint;

import com.matrixboot.validation.validator.IdCardValidator;
import com.matrixboot.validation.validator.MacValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * create in 2021/11/30 3:56 下午
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MacValidator.class)
public @interface Mac {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
