package com.relax.relax.common.enums;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.template.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 基础sql枚举
 * 进行不同类型操作的sql拼接并执行
 */
@Slf4j
public enum BaseSqlEnum {
    INSERT((entity) -> {
        String sql = initSql(SqlTemplate.INSERT, entity);
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
    }),
    UPDATE_BY_ID((entity) -> {
        String sql = initSql(SqlTemplate.UPDATE_BY_ID, entity);
        try {
            SpringUtil.getBean(DataSource.class)
                    .getConnection()
                    .createStatement()
                    .execute(sql);
        } catch (SQLException e) {
            log.error("[relax] execute update sql error.\n" +
                            "sql is : {},\n" +
                            "error message is :{}\n",
                    sql, e.getMessage());
            return 0;
        }
        return 1;
    }),
    DELETE_BY_ID((entity)-> {
        String sql = initSql(SqlTemplate.DELETE_BY_ID, entity);
        try {
            SpringUtil.getBean(DataSource.class)
                    .getConnection()
                    .createStatement()
                    .execute(sql);
        } catch (SQLException e) {
            log.error("[relax] execute delete sql error.\n" +
                            "sql is : {},\n" +
                            "error message is :{}\n",
                    sql, e.getMessage());
            return 0;
        }
        return 1;
    })
    ;

    BaseSqlEnum(Function<Object, Integer> fn) {
        this.fn = fn;
    }

    private final Function<Object, Integer> fn;

    public int execute(Object entity) {
        if (isStandadRelaxEntity(entity.getClass()) == null) return 0;
        return fn.apply(entity);
    }

    /**
     * 初始化sql
     */
    private static String initSql(String sqlTemplate, Object entity) {
        if (Objects.equals(SqlTemplate.INSERT, sqlTemplate)) {
            return createInsertSql(sqlTemplate, entity, entity.getClass().getAnnotation(RelaxEntity.class));
        }
        if (Objects.equals(SqlTemplate.UPDATE_BY_ID, sqlTemplate)) {
            return createUpdateSql(sqlTemplate, entity, entity.getClass().getAnnotation(RelaxEntity.class));
        }
        if (Objects.equals(SqlTemplate.DELETE_BY_ID, sqlTemplate)) {
            return createDeleteSql(sqlTemplate, entity, entity.getClass().getAnnotation(RelaxEntity.class));
        }
        return createInsertSql(sqlTemplate, entity, entity.getClass().getAnnotation(RelaxEntity.class));
    }

    /**
     * 创建新增sql
     */
    private static String createInsertSql(String sqlTemplate, Object entity, RelaxEntity relaxEntity) {
        StringBuilder colum = new StringBuilder();
        StringBuilder val = new StringBuilder();
        for (Map.Entry<String, Object> entry : JSON.parseObject(JSON.toJSONString(entity)).entrySet()) {
            colum.append("`");
            colum.append(entry.getKey().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase());
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

    /**
     * 创建根据id更新sql
     */
    private static String createUpdateSql(String sqlTemplate, Object entity, RelaxEntity relaxEntity) {

        StringBuilder set = new StringBuilder();
        JSONObject values = JSON.parseObject(JSON.toJSONString(entity));
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            set.append("`");
            set.append(entry.getKey().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase());
            set.append("`");
            set.append(" = ");
            set.append("'");
            set.append(entry.getValue());
            set.append("'");
            set.append(",");
        }
        set.deleteCharAt(set.lastIndexOf(","));
        // todo 2023年12月18日 增加sql校验,防止sql注入攻击

        return String.format(sqlTemplate, relaxEntity.tableName(), set, values.get("id"));
    }

    /**
     * 创建根据id删除sql
     */
    private static String createDeleteSql(String sqlTemplate, Object entity, RelaxEntity relaxEntity) {
        // todo 2023年12月18日 增加sql校验,防止sql注入攻击
        JSONObject values = JSON.parseObject(JSON.toJSONString(entity));
        return String.format(sqlTemplate, relaxEntity.tableName(), values.get("id"));
    }

    /**
     * 检验是否符合注解标准
     */
    private static RelaxEntity isStandadRelaxEntity(Class<?> eneityClass) {
        if (!eneityClass.isAnnotationPresent(RelaxEntity.class)) {
            log.error("[relax] Insert error! The annotation @RelaxEntity should mark on your entity for this service!");
            return null;
        }
        RelaxEntity relaxEntity = eneityClass.getAnnotation(RelaxEntity.class);
        if (StrUtil.isEmpty(relaxEntity.tableName())) {
            log.error("[relax] Insert error! The 'tableName' attribute at annotation @RelaxEntity must be filled!");
            return null;
        }
        return relaxEntity;
    }


}
