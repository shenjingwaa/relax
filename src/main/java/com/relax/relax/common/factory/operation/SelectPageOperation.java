package com.relax.relax.common.factory.operation;

import com.relax.relax.common.factory.SqlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
public class SelectPageOperation extends SqlOperation{

    private final JdbcTemplate jdbcTemplate;

    private final SelectListOperation selectListOperation;


    @Override
    public Map<String, Object> executeSql(HttpServletRequest request, Object param) {
        long pageNum = Long.parseLong(request.getParameter("pageNum"));
        long pageSize = Long.parseLong(request.getParameter("pageSize"));

        Class<?> targetClass = param.getClass();
        HashMap<String, Object> result = new HashMap<>();
        String selectListSql = "select * from relaxTableName where 1=1 relaxSelectSql ";
        selectListSql = selectListSql.replace("relaxTableName", getTableName(targetClass));
        List<String> args = selectListOperation.prepareSqlProperties(targetClass, param, selectListSql);
        if (Objects.isNull(args) || args.isEmpty()) {
            result.put("list", new ArrayList<>());
            result.put("msg", "Select page statement exception.");
            return result;
        }

        selectListSql = args.remove(args.size() - 1);
        String suffix = " limit " + (pageNum - 1) * pageSize + "," + pageSize;
        result.put("page", jdbcTemplate.queryForList(selectListSql + suffix, args.toArray()));
        selectListSql = selectListSql.replace("*", "count(1) as total");
        result.put("total", jdbcTemplate.queryForObject(selectListSql, Object.class,args.toArray()));

        return result;
    }

    @Override
    public boolean check(SqlType sqlEnum) {
        return Objects.equals(sqlEnum, SqlType.SELECT_PAGE);
    }

    public SelectPageOperation(JdbcTemplate jdbcTemplate, SelectListOperation selectListOperation) {
        this.jdbcTemplate = jdbcTemplate;
        this.selectListOperation = selectListOperation;
    }
}
