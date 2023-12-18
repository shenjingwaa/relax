package com.relax.relax.common.executor;

import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.utils.StringUtil;

public class TableNameExtractor {
    public static String getTableName(Class<?> entityClass) {
        RelaxEntity relaxEntityAnnotation = entityClass.getAnnotation(RelaxEntity.class);
        String defaultTableName = StringUtil.camelCaseToUnderscore(entityClass.getSimpleName());

        return relaxEntityAnnotation.enable() ?
                relaxEntityAnnotation.tableName().isEmpty() ? defaultTableName : relaxEntityAnnotation.tableName() :
                defaultTableName;
    }
}
