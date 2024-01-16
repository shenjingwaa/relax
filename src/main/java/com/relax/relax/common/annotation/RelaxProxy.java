package com.relax.relax.common.annotation;

import com.relax.relax.common.enums.ProxyType;
import com.relax.relax.common.proxy.RelaxViewProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelaxProxy {
    Class<?> relaxClass();

    Class<? extends RelaxViewProxy> afterClass() default RelaxViewProxy.class;

    ProxyType proxyType();
}
