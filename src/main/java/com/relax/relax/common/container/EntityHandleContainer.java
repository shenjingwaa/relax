package com.relax.relax.common.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityHandleContainer {
    private final static Map entityHandleContainer = new ConcurrentHashMap();

    public static <T> void mapping(Class<?> handleClass, Class<T> baseEntityClass) {
        entityHandleContainer.put(handleClass, baseEntityClass);
    }

    public static Class<?> get(Class<?> handleClass) {
        return (Class<?>) entityHandleContainer.get(handleClass);
    }
}
