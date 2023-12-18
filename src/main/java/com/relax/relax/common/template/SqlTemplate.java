package com.relax.relax.common.template;

/**
 * sql生成模板
 */
public class SqlTemplate {

    /**
     * 插入
     */
    public static final String INSERT = "insert into `%s`(%s) values(%s)";
    /**
     * 根据ID修改
     */
    public static final String UPDATE_BY_ID = "update `%s` SET %s  where `id` = '%s'";

    /**
     * 根据ID删除
     */
    public static final String DELETE_BY_ID = "delete from `%s` where `id` = '%s'";
}
