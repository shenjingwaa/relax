package com.relax.relax.common.factory;

import com.relax.relax.common.factory.operation.SqlOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 生成基础业务sql
 */
@Slf4j
@Component("sqlOperationFactory")
public class SqlOperationFactory {

    @Autowired
    private List<SqlOperation> sqlOperationList;


    /**
     * 获取待执行的sql
     */
    public Object submit(BaseSqlEnum sqlEnum,
                         HttpServletRequest request,
                         Object param) {
        for (SqlOperation operation : sqlOperationList) {
            Object result = operation.execute(sqlEnum, request, param);
            if (Objects.nonNull(result)){
                return result;
            }
        }
        throw new IllegalArgumentException("Unsupported operation: " + sqlEnum.name());
    }


}
