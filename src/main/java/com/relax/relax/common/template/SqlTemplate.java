package com.relax.relax.common.template;

/**
 * sql生成模板
 */
public class SqlTemplate {

    public static final String UPDATE_BY_ID = "UPDATE `%s` SET %s  where `id` = %s";
    /**
     * 插入
     */
    public static String INSERT = "insert into `%s`(%s) values(%s)";
}
