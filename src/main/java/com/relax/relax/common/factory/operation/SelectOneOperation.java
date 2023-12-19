package com.relax.relax.common.factory.operation;

import com.relax.relax.common.factory.SqlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class SelectOneOperation extends SqlOperation{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> executeSql(HttpServletRequest request, Object param) {
        HashMap<String, Object> result = new HashMap<>();
        Class<?> paramClass = param.getClass();
        String selectOneSql = "select * from relaxTableName where relaxRowIdName = ? ";
        selectOneSql = selectOneSql.replace("relaxTableName", getTableName(paramClass));
        selectOneSql = selectOneSql.replace("relaxRowIdName", getUniqueColumn(paramClass));

        Object idValue = getUniqueColumnValue(param);
        if (Objects.isNull(idValue)) {
            log.error("[relax] Obtaining unique value is null.");
            return result;
        }
        result.put("info", jdbcTemplate.queryForMap(selectOneSql, idValue));
        return result;
    }

    @Override
    public boolean check(SqlType sqlEnum) {
        return Objects.equals(sqlEnum, SqlType.SELECT_ONE);
    }

    public SelectOneOperation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
