package com.relax.relax.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelaxEntity {

    /**
     * 表名
     */
    String tableName();

    /**
     * 表注释
     */
    String comment() default "";

    /**
     * 开关
     */
    boolean enable() default true;
}
