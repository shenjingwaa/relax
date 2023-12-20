package com.relax.relax.common.executor;

import com.relax.relax.common.enums.SqlType;
import com.relax.relax.common.operation.SqlOperation;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 生成基础业务sql
 */
@Slf4j
public class SqlOperationExecutor {

    private final List<SqlOperation> sqlOperationList;


    /**
     * 获取待执行的sql
     */
    public Object submit(SqlType sqlEnum,
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

    public SqlOperationExecutor(List<SqlOperation> sqlOperationList) {
        this.sqlOperationList = sqlOperationList;
    }


}
