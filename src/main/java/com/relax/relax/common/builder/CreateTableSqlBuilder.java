package com.relax.relax.common.builder;

import com.relax.relax.common.executor.TableNameExtractor;

import java.util.Arrays;

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
        createTableSql.append(");");

        return createTableSql.toString();
    }

    private String getTableName() {
        return TableNameExtractor.getTableName(entityClass);
    }
}
