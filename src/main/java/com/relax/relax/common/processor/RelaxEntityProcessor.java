package com.relax.relax.common.processor;


import java.sql.Connection;
import java.sql.SQLException;

public class RelaxEntityProcessor {
    public void processEntityClass(Class<?> entityClass, Connection connection) throws SQLException {
        EntityTableProcessor tableProcessor = new EntityTableProcessor(entityClass);
        String tableName = tableProcessor.getTableName();

        if (tableProcessor.shouldEnableTableCreation()) {
            if (!tableProcessor.doesTableExist(connection, tableName)) {
                String createTableSql = tableProcessor.buildCreateTableSql();
                tableProcessor.executeCreateTableSql(connection, createTableSql);
            }
        }
    }
}
