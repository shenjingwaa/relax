package com.relax.relax.common.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtil {

    private static ApplicationContext context;


    public static <T> T getBean(Class<T> beanType) {
        return context.getBean(beanType);
    }

    public static void setContext(ApplicationContext context) {
        SpringUtil.context = context;
    }

    private SpringUtil() {
    }
}
