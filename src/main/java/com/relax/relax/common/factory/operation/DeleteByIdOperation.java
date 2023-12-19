package com.relax.relax.common.factory.operation;

import com.relax.relax.common.factory.BaseSqlEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class DeleteByIdOperation extends SqlOperation {

    private final JdbcTemplate jdbcTemplate;

    public DeleteByIdOperation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, Object> executeSql(HttpServletRequest request, Object param) {
        Class<?> targetClass = param.getClass();
        HashMap<String, Object> result = new HashMap<>();
        String deleteSql = "delete from tableName where relaxRowIdName = ?";
        deleteSql = deleteSql.replace("tableName", getTableName(targetClass));
        String relaxRowIdName = getUniqueColumn(targetClass);
        deleteSql = deleteSql.replace("relaxRowIdName", relaxRowIdName);

        Field field = null;
        try {
            field = targetClass.getDeclaredField(relaxRowIdName);
            field.setAccessible(true);
            Object idValue = field.get(param);
            if (Objects.isNull(idValue)) {
                log.error("[relax] Obtaining unique value is null.");
                result.put("effectRow",0);
                return result;
            }
            result.put("effectRow",jdbcTemplate.update(deleteSql,idValue));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("[relax] Exception in obtaining unique value.");
        }
        return result;
    }

    @Override
    public boolean check(BaseSqlEnum sqlEnum) {
        return Objects.equals(sqlEnum,BaseSqlEnum.DELETE_BY_ID);
    }
}
