package com.relax.relax.common.annotation;

import com.relax.relax.RelaxAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启框架扫描
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RelaxAutoConfiguration.class)
public @interface EnableRelax {
}
