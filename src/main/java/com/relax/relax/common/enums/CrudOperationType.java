package com.relax.relax.common.enums;

import lombok.Getter;

@Getter
public enum CrudOperationType {
    ADD(ProxyMethodType.ADD, SqlType.INSERT),
    UPDATE(ProxyMethodType.UPDATE, SqlType.UPDATE_BY_ID),
    DELETE(ProxyMethodType.DELETE, SqlType.DELETE_BY_ID),
    PAGE(ProxyMethodType.PAGE, SqlType.SELECT_PAGE),
    LIST(ProxyMethodType.LIST, SqlType.SELECT_LIST),
    INFO(ProxyMethodType.INFO, SqlType.SELECT_BY_ID);

    private final ProxyMethodType proxyMethodType;
    private final SqlType sqlType;

    CrudOperationType(ProxyMethodType proxyMethodType, SqlType sqlType) {
        this.proxyMethodType = proxyMethodType;
        this.sqlType = sqlType;
    }

}
