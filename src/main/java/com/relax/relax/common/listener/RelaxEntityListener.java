package com.relax.relax.common.listener;

import com.relax.relax.common.annotation.EnableRelax;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.processor.RelaxEntityProcessor;
import com.relax.relax.common.properties.RelaxConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class RelaxEntityListener implements ApplicationListener<ApplicationReadyEvent> {

    private final RelaxConfigProperties configProperties;

    private final DataSource dataSource;

    public RelaxEntityListener(RelaxConfigProperties configProperties, DataSource dataSource) {
        this.configProperties = configProperties;
        this.dataSource = dataSource;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Class<?> mainClass = event.getSpringApplication().getMainApplicationClass();
        if (mainClass.isAnnotationPresent(EnableRelax.class) && mainClass.getAnnotation(EnableRelax.class).isEnable()) {
            createTables();
        }
    }

    private void createTables() {
        Set<Class<?>> classes = scanEntities();

        try (Connection connection = dataSource.getConnection()) {
            for (Class<?> entityClass : classes) {
                RelaxEntityProcessor processor = new RelaxEntityProcessor();
                processor.processEntityClass(entityClass, connection);
            }
        } catch (SQLException e) {
            log.error("auto create table error: " + e.getMessage());
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
}