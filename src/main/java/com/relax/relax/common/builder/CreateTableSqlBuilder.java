package com.relax.relax.common.builder;

import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.executor.TableNameExtractor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class CreateTableSqlBuilder {
    private final Class<?> entityClass;

    public CreateTableSqlBuilder(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public String buildCreateTableSql() {
        StringBuilder createTableSql = new StringBuilder("CREATE TABLE " + getTableName() + " (");

        Arrays.stream(entityClass.getDeclaredFields())
                .forEach(field -> createTableSql.append(ColumnSqlBuilder.buildColumnSql(field)));

        createTableSql.delete(createTableSql.length() - 2, createTableSql.length());
        createTableSql.append(")");
        if (entityClass.isAnnotationPresent(RelaxEntity.class))
            createTableSql.append(" COMMENT = '").append(entityClass.getAnnotation(RelaxEntity.class).comment()).append("' ");
        createTableSql.append(";");

        log.info("[relax] Table SQL for {} generation successful.",getTableName());
        return createTableSql.toString();
    }

    private String getTableName() {
        return TableNameExtractor.getTableName(entityClass);
    }
}
