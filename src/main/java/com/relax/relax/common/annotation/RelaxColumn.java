package com.relax.relax.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelaxColumn {

    /**
     * 列名
     */
    String name() default "";

    String type() default "";

    String length() default "";
}
