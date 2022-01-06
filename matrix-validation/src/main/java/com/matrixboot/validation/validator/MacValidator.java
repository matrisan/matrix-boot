package com.matrixboot.validation.validator;

import cn.hutool.core.lang.Validator;
import com.matrixboot.validation.constraint.IdCard;
import com.matrixboot.validation.constraint.Mac;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>
 * create in 2021/11/30 3:50 下午
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class MacValidator implements ConstraintValidator<Mac, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Validator.isMac(s);
    }
}
