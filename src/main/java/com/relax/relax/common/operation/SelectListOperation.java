package com.relax.relax.common.operation;

import com.relax.relax.common.annotation.RelaxColumn;
import com.relax.relax.common.enums.QueryType;
import com.relax.relax.common.enums.SqlType;
import com.relax.relax.common.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class SelectListOperation extends SqlOperation{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public <E> Map<String, Object> executeSql(HttpServletRequest request, Object param, Class<E> resultClass) {
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
        String relaxSelectItemForAllMatch = "and relaxFieldName = ? ";
        String relaxSelectItemForLeftLike = "and relaxFieldName like CONCAT('%',?,'') ";
        String relaxSelectItemForRightLike = "and relaxFieldName like CONCAT('',?,'%') ";
        String relaxSelectItemForAllLike = "and relaxFieldName like CONCAT('%',?,'%') ";

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
            RelaxColumn relaxColumn = field.getAnnotation(RelaxColumn.class);
            QueryType queryType = relaxColumn.queryType();
            if (Objects.equals(QueryType.ALL_MATCH, queryType)) relaxSelectSql.append(relaxSelectItemForAllMatch.replace("relaxFieldName", RegexUtil.camelCaseToUnderscore(field.getName())));
            if (Objects.equals(QueryType.LEFT_LIKE, queryType)) relaxSelectSql.append(relaxSelectItemForLeftLike.replace("relaxFieldName", RegexUtil.camelCaseToUnderscore(field.getName())));
            if (Objects.equals(QueryType.RIGHT_LIKE, queryType)) relaxSelectSql.append(relaxSelectItemForRightLike.replace("relaxFieldName", RegexUtil.camelCaseToUnderscore(field.getName())));
            if (Objects.equals(QueryType.ALL_LIKE, queryType)) relaxSelectSql.append(relaxSelectItemForAllLike.replace("relaxFieldName", RegexUtil.camelCaseToUnderscore(field.getName())));

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
