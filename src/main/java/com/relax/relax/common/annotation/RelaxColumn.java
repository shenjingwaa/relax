package com.relax.relax.common.annotation;

import com.relax.relax.common.enums.QueryType;
import com.relax.relax.common.enums.RangeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelaxColumn {

    /**
     * 对应的表列名,不写默认用实体的属性名转换
     */
    String name() default "";

    /**
     * 数据表中的类型
     *
     * @see com.relax.relax.common.constants.SqlFieldType
     */
    String type() default "";

    /**
     * 数据表中的长度设置
     */
    String length() default "";

    /**
     * page list 查询时的查询模式,填入的值参考以下
     * @see com.relax.relax.common.enums.QueryType
     */
    QueryType queryType() default QueryType.ALL_MATCH;

    /**
     * page list 查询时范围筛选
     * @see RangeType
     */
    RangeType rangeType() default RangeType.NONE;

    /**
     * 范围查询时所对应的数据表字段
     *
     */
    String rangeField() default "";

    /**
     * 字段是参与表结构生成
     */
    boolean generate() default true;

}
