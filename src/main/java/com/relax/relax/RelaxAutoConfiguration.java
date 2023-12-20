package com.relax.relax;

import com.relax.relax.common.executor.SqlOperationExecutor;
import com.relax.relax.common.handler.BaseMappingHandler;
import com.relax.relax.common.listener.RelaxEntityListener;
import com.relax.relax.common.operation.*;
import com.relax.relax.common.properties.RelaxConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

@EnableConfigurationProperties(RelaxConfigProperties.class)
@Configuration
public class RelaxAutoConfiguration {

    @Bean
    public InsertOperation insertOperation(JdbcTemplate jdbcTemplate) {
        return new InsertOperation(jdbcTemplate);
    }

    @Bean
    public UpdateByIdOperation updateByIdOperation(JdbcTemplate jdbcTemplate) {
        return new UpdateByIdOperation(jdbcTemplate);
    }

    @Bean
    public DeleteByIdOperation deleteByIdOperation(JdbcTemplate jdbcTemplate) {
        return new DeleteByIdOperation(jdbcTemplate);
    }

    @Bean
    public SelectListOperation selectListOperation(JdbcTemplate jdbcTemplate) {
        return new SelectListOperation(jdbcTemplate);
    }

    @Bean
    public SelectOneOperation selectOneOperation(JdbcTemplate jdbcTemplate) {
        return new SelectOneOperation(jdbcTemplate);
    }

    @Bean
    public SelectPageOperation selectPageOperation(JdbcTemplate jdbcTemplate, SelectListOperation selectListOperation) {
        return new SelectPageOperation(jdbcTemplate, selectListOperation);
    }

    @Bean
    public SqlOperationExecutor sqlOperationExecutor(List<SqlOperation> sqlOperationList) {
        return new SqlOperationExecutor(sqlOperationList);
    }

    @Bean
    public BaseMappingHandler baseMappingHandler(ApplicationContext context) {
        return new BaseMappingHandler(context);
    }

    @Bean
    public RelaxEntityListener relaxEntityListener(RelaxConfigProperties relaxConfigProperties, DataSource dataSource) {
        return new RelaxEntityListener(relaxConfigProperties, dataSource);
    }

}
