package com.relax.relax.common.enums;

/**
 * 列表查询接口中(page/list)查询条件的类型
 */
public enum QueryType {

    /**
     * 全词匹配 ( = )
     */
    ALL_MATCH,

    /**
     * 左侧模糊匹配 ( %xxx )
     */
    LEFT_LIKE,

    /**
     * 右侧模糊匹配 ( xxx% )
     */
    RIGHT_LIKE,

    /**
     * 左右模糊匹配 ( %xxx% )
     */
    ALL_LIKE;

}