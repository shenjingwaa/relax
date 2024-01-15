package com.relax.relax.common.utils;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class SpringUtil {

    private static ApplicationContext context;


    public static <T> T getBean(Class<T> beanType) {
        return context.getBean(beanType);
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static void setContext(ApplicationContext context) {
        SpringUtil.context = context;
    }

    public static void addBean(Object bean, String beanName) {
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) context;
        ConfigurableListableBeanFactory beanFactory = genericApplicationContext.getBeanFactory();
        beanFactory.registerSingleton(beanName, bean);
    }

    private SpringUtil() {
    }
}
