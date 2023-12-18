package com.relax.relax.common.enums;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.relax.relax.common.annotation.RelaxEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;

public enum SqlEnum {
    INSERT((entity) -> {
        RelaxEntity relaxEntity = entity.getClass().getAnnotation(RelaxEntity.class);

        JSONObject jo = JSON.parseObject(JSON.toJSONString(entity));
        String template = "insert into `%s`(%s) values(%s)";

        StringBuilder colum = new StringBuilder();
        StringBuilder val = new StringBuilder();
        for (Map.Entry<String, Object> entry : jo.entrySet()) {
            colum.append("`");
            colum.append(entry.getKey());
            colum.append("`");
            colum.append(",");

            val.append("'");
            val.append(entry.getValue());
            val.append("'");
            val.append(",");

        }
        colum.deleteCharAt(colum.lastIndexOf(","));
        val.deleteCharAt(val.lastIndexOf(","));


        String sql = String.format(template, relaxEntity.tableName(),colum,val);
        System.out.println(sql);
        try {
            Connection connection = SpringUtil.getBean(DataSource.class).getConnection();
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return 1;
    });

    SqlEnum(Function<Object, Integer> fn) {
        this.fn = fn;
    }

    private final Function<Object, Integer> fn;

    public void execute(Object entity) {
        fn.apply(entity);
    }


}
