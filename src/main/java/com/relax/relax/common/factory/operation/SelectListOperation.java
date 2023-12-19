package com.relax.relax.common.factory.operation;

import com.relax.relax.common.factory.BaseSqlEnum;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Component
public class SelectListOperation extends SqlOperation{
    private static final String SELECT_LIST = "select * from ? where 1=1 ? ";

    @Override
    public Map<String, Object> executeSql(HttpServletRequest request, Object param) {
        return null;
    }

    @Override
    public boolean check(BaseSqlEnum sqlEnum) {
        return Objects.equals(sqlEnum,BaseSqlEnum.SELECT_LIST);
    }
}
