package com.relax.relax.common.operation;

import com.relax.relax.common.enums.SqlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
public class SelectPageOperation extends SqlOperation{

    private final JdbcTemplate jdbcTemplate;

    private final SelectListOperation selectListOperation;


    @Override
    public <E> Map<String, Object> executeSql(HttpServletRequest request, Object param, Class<E> resultClass) {
        String num = request.getParameter("pageNum");
        if (Objects.isNull(num) || num.isEmpty() || Long.parseLong(num) <= 0) {
            num = "1";
        }
        String size = request.getParameter("pageSize");
        if (Objects.isNull(size) || size.isEmpty() || Long.parseLong(size) <= 0) {
            size = "10";
        }
        long pageNum = Long.parseLong(num);
        long pageSize = Long.parseLong(size);

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
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(selectListSql + suffix, args.toArray());


        result.put("page", maps);
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
