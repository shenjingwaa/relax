package com.relax.relax.common.utils;

import com.relax.relax.common.domain.RelaxResult;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BeanUtil {
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
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
                field.set(object, map.get(field.getName()));
            }
        } catch (Exception e) {
            log.error("map to bean error");
        }
        return object;
    }
}
