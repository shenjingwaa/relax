package com.relax.relax.common.enums;

/**
 * 主键生成策略
 */
public enum IdType {

    /**
     * 自增
     */
    AUTO_INCREMENT,

    /**
     * UUID,带 '-'
     */
    UUID,

    /**
     * UUID,不带 '-'
     */
    SIMPLE_UUID,

    /**
     * 雪花算法
     */
    SNOW_FLAKE;

}
