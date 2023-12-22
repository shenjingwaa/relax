package com.relax.relax.common.processor;


import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.builder.CreateTableSqlBuilder;
import com.relax.relax.common.executor.DatabaseExecutor;
import com.relax.relax.common.executor.TableNameExtractor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class EntityTableProcessor {
    private final Class<?> entityClass;

    public EntityTableProcessor(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public String getTableName() {
        return TableNameExtractor.getTableName(entityClass);
    }

    public boolean shouldEnableTableCreation() {
        RelaxEntity relaxEntityAnnotation = entityClass.getAnnotation(RelaxEntity.class);
        return relaxEntityAnnotation != null && relaxEntityAnnotation.enable();
    }

    public boolean doesTableExist(Connection connection, String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);

            return tables.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public String buildCreateTableSql() {
        CreateTableSqlBuilder createTableSqlBuilder = new CreateTableSqlBuilder(entityClass);
        return createTableSqlBuilder.buildCreateTableSql();
    }

    public void executeCreateTableSql(Connection connection, String createTableSql) throws SQLException {
        log.info("[relax] Start to create tables.");
        DatabaseExecutor databaseExecutor = new DatabaseExecutor();
        databaseExecutor.executeCreateTableSql(connection, createTableSql, getTableName());
        log.info("[relax] Create table end.");
    }
}
