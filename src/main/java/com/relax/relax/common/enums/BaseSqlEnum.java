package com.relax.relax.common.enums;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.relax.relax.common.annotation.RelaxColumn;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.annotation.RelaxId;
import com.relax.relax.common.template.SqlTemplate;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
        SpringUtil.getBean(JdbcTemplate.class).execute(initSql(SqlTemplate.INSERT, entity));
        return 1;
    }),
    UPDATE_BY_ID((entity) -> {
        SpringUtil.getBean(JdbcTemplate.class).execute(initSql(SqlTemplate.UPDATE_BY_ID, entity));
        return 1;
    }),
    DELETE_BY_ID((entity) -> {
        SpringUtil.getBean(JdbcTemplate.class).execute(initSql(SqlTemplate.DELETE_BY_ID, entity));
        return 1;
    }),
    SELECT_ONE((param) -> SpringUtil.getBean(JdbcTemplate.class)
            .queryForMap(initSelectSql(SqlTemplate.SELECT_ONE, param))),
    SELECT_LIST((param) -> SpringUtil.getBean(JdbcTemplate.class)
            .queryForList(initSelectSql(SqlTemplate.SELECT_LIST, param))),

    ;

    BaseSqlEnum(Function<Object, Object> fn) {
        this.fn = fn;
    }

    private final Function<Object, Object> fn;

    public Object execute(Object entity) {
        if (isStandadRelaxEntity(entity.getClass()) == null) return 0;
        return fn.apply(entity);
    }

    /**
     * 初始化变更sql
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
        return "";
    }

    /**
     * 初始化查询相关sql
     */
    private static String initSelectSql(String sqlTemplate, Object param) {
        Class<?> paramClass = param.getClass();
        if (Objects.equals(SqlTemplate.SELECT_ONE, sqlTemplate)) {
            String uniqueFieldName = getUniqueFieldName(param);
            Field field = null;
            try {
                field = paramClass.getDeclaredField(uniqueFieldName);
                field.setAccessible(true);

                return createSelectOneSql(sqlTemplate, field.get(param).toString(), paramClass.getAnnotation(RelaxEntity.class));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else if (Objects.equals(SqlTemplate.SELECT_LIST, sqlTemplate)) {
            JSONObject jsonMap = JSON.parseObject(JSON.toJSONString(param));
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                sb.append(" and ");
                sb.append("`").append(entry.getKey().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase()).append("`");
                sb.append("=");
                sb.append("'").append(entry.getValue()).append("'");
            }
            return createSelectOneSql(sqlTemplate, sb.toString(), paramClass.getAnnotation(RelaxEntity.class));
        } else {
            return "";
        }
    }

    /**
     * 获取传入对象的唯一标识字段名
     */
    public static String getUniqueFieldName(Object param) {
        String uniqueFieldName = "id";
        for (Field field : param.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(RelaxId.class)) {
                uniqueFieldName = field.getName();
                break;
            }
        }
        return uniqueFieldName;
    }

    /**
     * 查询详情信息sql
     */
    private static String createSelectOneSql(String sqlTemplate, String uniqueFieldName, RelaxEntity annotation) {
        String format = String.format(sqlTemplate, annotation.tableName(), uniqueFieldName);
        log.debug(format);
        return format;
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
        // todo 2023年12月18日 将id更换为动态主键
        return String.format(sqlTemplate, relaxEntity.tableName(), set, values.get("id"));
    }

    /**
     * 创建根据id删除sql
     */
    private static String createDeleteSql(String sqlTemplate, Object entity, RelaxEntity relaxEntity) {
        // todo 2023年12月18日 增加sql校验,防止sql注入攻击
        JSONObject values = JSON.parseObject(JSON.toJSONString(entity));
        // todo 2023年12月18日 将id更换为动态主键
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
