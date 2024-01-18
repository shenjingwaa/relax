package com.relax.relax.common.enums;

public enum RangeType {

    /**
     * > 大于
     */
    GT(">"),

    /**
     * < 小于
     */
    LT("<"),

    /**
     * >= 大于等于
     */
    GE(">="),

    /**
     * <= 小于等于
     */
    LE("<="),

    /**
     * 标记性的类型,用于默认值
     */
    NONE("=")
    ;

    public String symbol;
    RangeType(String symbol) {
        this.symbol = symbol;
    }
}
