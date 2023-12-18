package com.relax.relax.common.enums;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.template.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public enum BaseSqlEnum {
    INSERT((entity) -> {
        Class<?> eneityClass = entity.getClass();
        if (!eneityClass.isAnnotationPresent(RelaxEntity.class)) {
            log.error("[relax] Insert error! The annotation @RelaxEntity should mark on your entity for this service!");
            return 0;
        }
        StringBuilder colum = new StringBuilder();
        StringBuilder val = new StringBuilder();
        for (Map.Entry<String, Object> entry : JSON.parseObject(JSON.toJSONString(entity)).entrySet()) {
            colum.append("`");
            colum.append(entry.getKey());
            colum.append("`");
            colum.append(",");

            val.append("'");
            val.append(entry.getValue());
            val.append("'");
            val.append(",");

        }
        colum.deleteCharAt(colum.lastIndexOf(","));
        val.deleteCharAt(val.lastIndexOf(","));

        RelaxEntity relaxEntity = eneityClass.getAnnotation(RelaxEntity.class);
        String sql = String.format(SqlTemplate.INSERT, relaxEntity.tableName(), colum, val);
        log.debug("[relax] execute sql : {}", sql);
        try {
            Connection connection = SpringUtil.getBean(DataSource.class).getConnection();
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            log.error("[relax] execute add sql error.\n" +
                            "sql is : {},\n" +
                            "error message is :{}\n",
                    sql, e.getMessage());
            return 0;
        }
        return 1;
    });

    BaseSqlEnum(Function<Object, Integer> fn) {
        this.fn = fn;
    }

    private final Function<Object, Integer> fn;

    public void execute(Object entity) {
        fn.apply(entity);
    }


}
