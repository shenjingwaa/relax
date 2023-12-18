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

    /**
     * 查询单个对象
     */
    public static final String SELECT_ONE = "select * from `%s` where `id` = '%s' ";

    /**
     * 列表查询
     */
    public static final String SELECT_LIST = "select * from `%s` where 1=1 %s ";
}
