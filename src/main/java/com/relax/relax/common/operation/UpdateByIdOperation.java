package com.relax.relax.common.operation;

import com.relax.relax.common.enums.SqlType;
import com.relax.relax.common.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class UpdateByIdOperation extends SqlOperation {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> executeSql(HttpServletRequest request, Object param) {
        String updateSql = "update relaxTableName SET relaxSet where relaxRowIdName = ?";
        Map<String, Object> result = new HashMap<>();
        Class<?> targetClass = param.getClass();
        String relaxRowIdName = getUniqueColumn(targetClass);

        updateSql = updateSql.replace("relaxTableName", getTableName(targetClass));
        updateSql = updateSql.replace("relaxRowIdName", RegexUtil.camelCaseToUnderscore(relaxRowIdName));

        List<String> args = prepareSqlProperties(targetClass, param, updateSql);
        if (Objects.isNull(args) || args.isEmpty()) {
            result.put("effectRow", 0);
            result.put("msg", "Combination update statement exception.");
            return result;
        }
        updateSql = args.remove(args.size() - 1);
        int update = jdbcTemplate.update(updateSql, args.toArray());
        result.put("effectRow", update);
        return result;
    }

    @Override
    public boolean check(SqlType sqlEnum) {
        return Objects.equals(sqlEnum, SqlType.UPDATE_BY_ID);
    }

    protected List<String> prepareSqlProperties(Class<?> targetClass,
                                                Object param,
                                                String updateSql
    ) {
        String uniqueValue = null;
        String relaxSetItem = "relaxFieldName = ?";
        StringBuilder relaxSet = new StringBuilder();
        List<String> args = new ArrayList<>();
        try {
            for (Field field : getRelaxField(targetClass)) {
                field.setAccessible(true);
                Object fieldValue = field.get(param);
                if (Objects.isNull(fieldValue)) {
                    continue;
                }
                if (Objects.equals(field.getName(), getUniqueColumn(targetClass))) {
                    uniqueValue = fieldValue.toString();
                    continue;
                }
                relaxSet.append(relaxSetItem.replace("relaxFieldName", RegexUtil.camelCaseToUnderscore(field.getName()))).append(",");
                args.add(fieldValue.toString());
            }
            relaxSet.deleteCharAt(relaxSet.lastIndexOf(","));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (args.isEmpty()) {
            return null;
        }
        updateSql = updateSql.replace("relaxSet", relaxSet);

        args.add(uniqueValue);
        args.add(updateSql);
        return args;
    }

    public UpdateByIdOperation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
