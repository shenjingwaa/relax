package com.relax.relax.common.enums;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.template.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;

/**
 * 基础sql枚举
 * 进行不同类型操作的sql拼接并执行
 */
@Slf4j
public enum BaseSqlEnum {
    INSERT((entity) -> {
        Class<?> eneityClass = entity.getClass();
        if (!eneityClass.isAnnotationPresent(RelaxEntity.class)) {
            log.error("[relax] Insert error! The annotation @RelaxEntity should mark on your entity for this service!");
            return 0;
        }
        RelaxEntity relaxEntity = eneityClass.getAnnotation(RelaxEntity.class);
        if (StrUtil.isEmpty(relaxEntity.tableName())) {
            log.error("[relax] Insert error! The 'tableName' attribute at annotation @RelaxEntity must be filled!");
            return 0;
        }

        String sql = createSql(SqlTemplate.INSERT, entity, relaxEntity);
        log.debug("[relax] execute sql : {}", sql);
        try {
            SpringUtil.getBean(DataSource.class)
                    .getConnection()
                    .createStatement()
                    .execute(sql);
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

    public int execute(Object entity) {
        return fn.apply(entity);
    }

    /**
     * 创建sql
     */
    private static String createSql(String sqlTemplate, Object entity, RelaxEntity relaxEntity) {
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
        // todo 2023年12月18日 增加sql校验,防止sql注入攻击

        return String.format(sqlTemplate, relaxEntity.tableName(), colum, val);
    }


}
