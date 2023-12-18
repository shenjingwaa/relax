package com.relax.relax.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelaxClass {

    /**
     * 动态增删改查接口的前缀
     * 前缀请不要携带 '/'
     */
    String prefix();

    /**
     * 需要自动生成的方法
     * @return
     */
    String[] methods() default {"add","delete","update","info","page","list"};

    /**
     * controller对应的表实体类型
     */
    Class<?> entityType();



}
