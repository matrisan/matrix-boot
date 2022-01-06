package com.matrixboot.validation.validator;

import cn.hutool.core.lang.Validator;
import com.matrixboot.validation.constraint.MobilePhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * <p>
 * create in 2021/11/30 3:50 下午
 *
 * @author shishaodong
 * @version 0.0.1
 */
public class MobileValidator implements ConstraintValidator<MobilePhone, String> {

    private static final Pattern CHINA_PATTERN = Pattern.compile("^[1]\\d{10}$");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Validator.isMoney(s);
    }
}
