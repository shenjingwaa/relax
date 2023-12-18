package com.relax.relax.common.listener;

import com.relax.relax.common.annotation.EnableRelax;
import com.relax.relax.common.annotation.RelaxColumn;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.annotation.RelaxId;
import com.relax.relax.common.properties.RelaxConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class RelaxEntityListener implements ApplicationListener<ApplicationReadyEvent> {

    private final RelaxConfigProperties configProperties;

    private final DataSource dataSource;

    public RelaxEntityListener(RelaxConfigProperties configProperties, DataSource dataSource) {
        this.configProperties = configProperties;
        this.dataSource = dataSource;
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        Class<?> mainClass = event.getSpringApplication().getMainApplicationClass();
        if (mainClass.isAnnotationPresent(EnableRelax.class) && mainClass.getAnnotation(EnableRelax.class).isEnable()) {
            createTables();
        }
    }

    private void createTables() {
        Set<Class<?>> classes = scanEntities();

        try (Connection connection = dataSource.getConnection()) {
            for (Class<?> entityClass : classes) {
                processEntityClass(entityClass, connection);
            }
        } catch (SQLException e) {
            log.error("auto create table error: " + e.getMessage());
        }
    }

    private void processEntityClass(Class<?> entityClass, Connection connection) throws SQLException {
        String tableName = entityClass.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        RelaxEntity relaxEntityAnnotation = entityClass.getAnnotation(RelaxEntity.class);

        if (!relaxEntityAnnotation.enable()) return;

        if (!relaxEntityAnnotation.tableName().isEmpty()) {
            tableName = relaxEntityAnnotation.tableName();
        }

        if (tableExists(tableName)) return;

        StringBuilder createTableSql = new StringBuilder("CREATE TABLE " + tableName + " (");

        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            String columnName = field.getName().toLowerCase();
            if (field.isAnnotationPresent(RelaxColumn.class)) {
                RelaxColumn columnAnnotation = field.getAnnotation(RelaxColumn.class);
                if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
                    columnName = columnAnnotation.name();
                }

            }
            columnName = "`"+columnName+"`";

            if (!field.isAnnotationPresent(RelaxColumn.class) || field.getAnnotation(RelaxColumn.class).type().isEmpty()) {
                Class<?> type = field.getType();
                if (type.equals(String.class)) {
                    createTableSql.append(columnName).append(" VARCHAR(255)");
                } else if (type.equals(Long.class)) {
                    createTableSql.append(columnName).append(" BIGINT");
                } else if (type.equals(Integer.class)) {
                    createTableSql.append(columnName).append(" INT");
                } else if (type.equals(Float.class) || type.equals(Double.class)) {
                    createTableSql.append(columnName).append(" DECIMAL(10,2)");
                } else if (type.equals(Date.class) || type.equals(LocalDateTime.class)) {
                    createTableSql.append(columnName).append(" DATETIME");
                } else if (type.equals(LocalDate.class)) {
                    createTableSql.append(columnName).append(" DATE");
                } else {
                    log.warn(type + "not default column type, please specify column type in @RelaxColumn");
                    continue;
                }
            } else {
                RelaxColumn relaxColumn = field.getAnnotation(RelaxColumn.class);
                createTableSql.append(columnName).append(relaxColumn.type());
                if (!relaxColumn.length().isEmpty()) createTableSql.append("(").append(relaxColumn.length()).append(")");
            }

            boolean isPrimaryKey = field.isAnnotationPresent(RelaxId.class);

            if (isPrimaryKey) {
                createTableSql.append(" PRIMARY KEY");
            }
            createTableSql.append(", ");
        }

        createTableSql.delete(createTableSql.length() - 2, createTableSql.length());
        createTableSql.append(");");

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql.toString());
            log.info("Table created: " + tableName);
        }
    }

    private Set<Class<?>> scanClasses(ClassPathScanningCandidateComponentProvider scanner) {
        Set<Class<?>> entityClasses = new HashSet<>();

        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(configProperties.getEntityLocations())) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                entityClasses.add(clazz);
            } catch (ClassNotFoundException e) {
                log.error("scan entity error" + e.getMessage());
            }
        }

        return entityClasses;
    }

    private Set<Class<?>> scanEntities() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(RelaxEntity.class));

        return scanClasses(scanner);
    }

    private boolean tableExists(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);

            return tables.next();
        } catch (SQLException e) {
            return false;
        }
    }
}