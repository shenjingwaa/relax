package com.relax.relax.common.operation;

import com.relax.relax.common.annotation.RelaxColumn;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.annotation.RelaxId;
import com.relax.relax.common.enums.SqlType;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class SqlOperation {

    /**
     * 创建sql
     */
    protected abstract <E> Map<String, Object> executeSql(HttpServletRequest request, Object param,Class<E> resultClass);

    protected abstract boolean check(SqlType sqlEnum);

    public <T> Object execute(SqlType sqlEnum, HttpServletRequest request, Object param,Class<T> resultClass) {
        if (check(sqlEnum)) return executeSql(request, param,resultClass);
        return null;
    }

    /**
     * 获取一个类中注有@RelaxId的字段名
     *
     * @param targetClass 待检测类
     * @return 字段名
     */
    public String getUniqueColumn(Class<?> targetClass) {
        isRelaxEntityClass(targetClass);
        return isClassHasRelaxId(targetClass);
    }
    protected Object getUniqueColumnValue(Object param) {
        String column = getUniqueColumn(param.getClass());
        Field field;
        try {
            field = param.getClass().getDeclaredField(column);
            field.setAccessible(true);
            return field.get(param);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("[relax] Exception in obtaining unique value.");
        }
        return null;
    }


    protected String getTableName(Class<?> targetClass) {
        String tableName = isRelaxEntityClass(targetClass).tableName();
        if (Objects.isNull(tableName) || tableName.isEmpty()) {
            log.error("[relax] The annotation @RelaxEntity should indicate the table name.");
            Thread.currentThread().interrupt();
        }
        return tableName;
    }

    protected List<Field> getRelaxField(Class<?> targetClass) {
        List<Field> fieldList = Arrays.stream(targetClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(RelaxColumn.class))
                .collect(Collectors.toList());
        if (fieldList.isEmpty()) {
            log.error("[relax] The class attribute marked with @RelaxEntity must contain at least one field labeled with @RelaxColumn.");
        }
        return fieldList;
    }

    /**
     * 检查一个类是否为注有@RelaxEntity的类
     *
     * @param targetClass 待检测类
     */
    private RelaxEntity isRelaxEntityClass(Class<?> targetClass) {
        RelaxEntity relaxEntity = targetClass.getAnnotation(RelaxEntity.class);
        if (Objects.isNull(relaxEntity)) {
            log.error("[relax] The class corresponding to the parameter must contain annotation @RelaxEntity.");
            Thread.currentThread().interrupt();
        }
        return relaxEntity;
    }

    /**
     * 检查一个类中是否有且仅有一个字段注有@RelaxId
     *
     * @param targetClass 待检测类
     * @return 字段名
     */
    private static String isClassHasRelaxId(Class<?> targetClass) {
        boolean idMask = false;
        String idFieldName = null;
        for (Field field : targetClass.getDeclaredFields()) {
            if (Objects.nonNull(field.getAnnotation(RelaxId.class))) {
                if (!idMask) {
                    idMask = true;
                    idFieldName = field.getName();
                    continue;
                }
                log.error("[relax] Only one field marked with @RelaxId can exist in a class marked with @RelaxEntity.");
                Thread.currentThread().interrupt();
            }
        }
        if (Objects.isNull(idFieldName)) {
            log.error("[relax] An unexpected error occurred while obtaining the field name,There must be a field with the @RelaxId annotation!");
            Thread.currentThread().interrupt();
        }
        return idFieldName;
    }


}

