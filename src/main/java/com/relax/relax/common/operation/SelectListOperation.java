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
@Component
public class SelectListOperation extends SqlOperation{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> executeSql(HttpServletRequest request, Object param) {
        Class<?> targetClass = param.getClass();
        HashMap<String, Object> result = new HashMap<>();
        String selectListSql = "select * from relaxTableName where 1=1 relaxSelectSql ";

        selectListSql = selectListSql.replace("relaxTableName", getTableName(targetClass));

        List<String> args = prepareSqlProperties(targetClass,param,selectListSql);
        if (Objects.isNull(args) || args.isEmpty()) {
            result.put("list", new ArrayList<>());
            result.put("msg", "Select statement exception.");
            return result;
        }
        selectListSql = args.remove(args.size() - 1);
        result.put("list",jdbcTemplate.queryForList(selectListSql,args.toArray()));
        return result;
    }

    protected List<String> prepareSqlProperties(Class<?> targetClass,
                                                Object param,
                                                String selectListSql
    ) {
        String relaxSelectItem = "and relaxFieldName = ? ";
        StringBuilder relaxSelectSql = new StringBuilder();
        List<String> args = new ArrayList<>();
        for (Field field : getRelaxField(targetClass)) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(param);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (Objects.isNull(fieldValue)) {
                continue;
            }
            relaxSelectSql.append(relaxSelectItem.replace("relaxFieldName", RegexUtil.camelCaseToUnderscore(field.getName())));
            args.add(fieldValue.toString());
        }
        if (args.isEmpty()) {
            args.add(selectListSql.replace("relaxSelectSql",""));
            return args;
        }
        args.add(selectListSql.replace("relaxSelectSql",relaxSelectSql.toString()));
        return args;
    }

    @Override
    public boolean check(SqlType sqlEnum) {
        return Objects.equals(sqlEnum, SqlType.SELECT_LIST);
    }

    public SelectListOperation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
