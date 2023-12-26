package com.relax.relax.common.executor;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class DatabaseExecutor {
    public void executeCreateTableSql(Connection connection, String createTableSql, String tableName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSql)) {
            preparedStatement.executeUpdate();
            log.info("[relax] Table created: " + tableName);
        }
    }
}
