package com.relax.relax.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BeanUtil {
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        return mapToBean(map, beanClass,false);
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass, boolean isToCamelCase) {
        return mapToBean(map, beanClass, isToCamelCase, '_');
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass, boolean isToCamelCase, char symbol) {
        if (map == null) {
            return null;
        }
        T object = null;
        try {
            object = beanClass.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                //获取属性类型
                int mod = field.getModifiers();
                //判断不能是静态和Final属性
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                //作用就是让我们在用反射时访问私有变量
                field.setAccessible(true);
                //将指定对象变量上此 Field 对象表示的字段设置为指定的新值.
                String fieldName = field.getName();
                if (isToCamelCase) {
                    fieldName = toCamelCase(fieldName, symbol,false);
                }
                field.set(object, map.get(fieldName));
            }
        } catch (Exception e) {
            log.error("map to bean error");
        }
        return object;
    }

    public static String toCamelCase(CharSequence name, char symbol,boolean isToLower) {
        if (null == name) {
            return null;
        }

        final String name2 = name.toString();
        if (name2.contains(String.valueOf(symbol))) {
            final int length = name2.length();
            final StringBuilder sb = new StringBuilder(length);
            boolean upperCase = false;
            for (int i = 0; i < length; i++) {
                char c = name2.charAt(i);

                if (c == symbol) {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        } else if (isToLower){
            return name2.toLowerCase();
        }else {
            return name2;
        }
    }

    public static <V> Map<String, V> mapKVToCamelCase(Map<String, V> map) {
        HashMap<String, V> beCamelCaseMap = new HashMap<>();
        map.forEach((k, v) -> {
            String camelCase = toCamelCase(k, '_',true);
            beCamelCaseMap.put(camelCase,v);
        });
        return beCamelCaseMap;
    }

    private BeanUtil() {
    }
}
