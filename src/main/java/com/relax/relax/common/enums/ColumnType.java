package com.relax.relax.common.enums;

import lombok.Getter;

@Getter
public enum ColumnType {
    STRING(" VARCHAR(255)"),
    LONG(" BIGINT"),
    INTEGER(" INT"),
    DECIMAL(" DECIMAL(10,2)"),
    DATETIME(" DATETIME"),
    LOCALDATETIME(" DATETIME"),
    DATE(" DATE");

    private final String sqlType;

    ColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

}
