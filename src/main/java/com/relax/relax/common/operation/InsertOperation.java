package com.relax.relax.common.operation;

import com.relax.relax.common.enums.SqlType;
import com.relax.relax.common.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class InsertOperation extends SqlOperation {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public <E> Map<String, Object> executeSql(HttpServletRequest request, Object param, Class<E> resultClass) {
        Map<String, Object> result = new HashMap<>();
        Class<?> targetClass = param.getClass();
        String insertSql = "insert into %s(%s) values(%s)";

        List<String> itemList = prepareSqlProperties(targetClass, param, new ArrayList<>(), insertSql, getTableName(targetClass));
        if (Objects.isNull(itemList) || itemList.isEmpty()) {
            result.put("effectRow", 0);
            return result;
        }
        insertSql = itemList.remove(itemList.size() - 1);
        int effectRow = jdbcTemplate.update(insertSql, itemList.toArray());
        result.put("effectRow", effectRow);
        return result;
    }

    @Override
    public boolean check(SqlType sqlEnum) {
        return Objects.equals(sqlEnum, SqlType.INSERT);
    }

    protected List<String> prepareSqlProperties(Class<?> targetClass,
                                                Object param,
                                                List<String> values,
                                                String baseSqlTemplate,
                                                String tableName) {
        List<Field> fieldList = getRelaxField(targetClass);
        if (fieldList.isEmpty()) {
            log.error("[relax] The class attribute marked with @RelaxEntity must contain at least one field labeled with @RelaxColumn.");
            return null;
        }

        StringBuilder attrSb = new StringBuilder();
        for (Field field : fieldList) {
            try {
                field.setAccessible(true);
                Object value = field.get(param);
                if (Objects.isNull(value)) {
                    continue;
                }
                // 拼接sql
                attrSb.append(RegexUtil.camelCaseToUnderscore(field.getName())).append(",");

                //存入sql中用到的参数
                values.add(value.toString());
            } catch (IllegalAccessException e) {
                log.error("[relax] Exception in obtaining value from param.");
                return null;
            }
        }
        attrSb.deleteCharAt(attrSb.lastIndexOf(","));

        //sql中的占位符个数匹配
        StringBuilder valueSb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            valueSb.append("?").append(",");
        }
        valueSb.deleteCharAt(valueSb.lastIndexOf(","));

        baseSqlTemplate = String.format(baseSqlTemplate, tableName, attrSb, valueSb);
        values.add(baseSqlTemplate);
        return values;
    }

    public InsertOperation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
