package com.relax.relax.common.annotation;

import com.relax.relax.common.enums.IdType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelaxId {

    /**
     * 主键生成方式
     */
    IdType value() default IdType.AUTO_INCREMENT;

}
