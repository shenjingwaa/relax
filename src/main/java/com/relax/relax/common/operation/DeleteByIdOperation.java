package com.relax.relax.common.operation;

import com.relax.relax.common.enums.SqlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class DeleteByIdOperation extends SqlOperation {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public <E> Map<String, Object> executeSql(HttpServletRequest request, Object param, Class<E> resultClass) {
        Class<?> targetClass = param.getClass();
        HashMap<String, Object> result = new HashMap<>();
        String deleteSql = "delete from relaxTableName where relaxRowIdName = ?";
        deleteSql = deleteSql.replace("relaxTableName", getTableName(targetClass));
        deleteSql = deleteSql.replace("relaxRowIdName", getUniqueColumn(targetClass));

        Object idValue = getUniqueColumnValue(param);
        if (Objects.isNull(idValue)) {
            log.error("[relax] Obtaining unique value is null.");
            result.put("effectRow", 0);
            return result;
        }
        result.put("effectRow", jdbcTemplate.update(deleteSql, idValue));
        return result;
    }

    @Override
    public boolean check(SqlType sqlEnum) {
        return Objects.equals(sqlEnum, SqlType.DELETE_BY_ID);
    }

    public DeleteByIdOperation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
