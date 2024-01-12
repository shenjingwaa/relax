package com.relax.relax.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.validation.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ValidationException;
import java.util.Objects;

/**
 * 校验工具
 */
@Slf4j
public class ValidationUtil {

    private static final SmartValidator smartValidator = SpringUtil.getBean(LocalValidatorFactoryBean.class);

    public static <T> void validate(T obj, @Nullable Object... validationHints) {
            Errors errors = new DirectFieldBindingResult(obj, obj.toString());
            ValidationUtils.invokeValidator(smartValidator, obj, errors, validationHints);
            for (ObjectError error : errors.getAllErrors()) {
                throw new ValidationException(error.getDefaultMessage());
            }
    }

}
