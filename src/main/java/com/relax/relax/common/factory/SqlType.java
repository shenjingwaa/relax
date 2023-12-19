package com.relax.relax.common.factory;

import com.relax.relax.common.annotation.RelaxId;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 基础sql枚举
 * 进行不同类型操作的sql拼接并执行
 */
@Slf4j
public enum SqlType {
    INSERT,
    UPDATE_BY_ID,
    DELETE_BY_ID,
    SELECT_ONE,
    SELECT_LIST,
    SELECT_PAGE,
    ;

    /**
     * 获取传入对象的唯一标识字段名
     */
    public static String getUniqueFieldName(Object param) {
        String uniqueFieldName = "id";
        for (Field field : param.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(RelaxId.class)) {
                uniqueFieldName = field.getName();
                break;
            }
        }
        return uniqueFieldName;
    }


}
