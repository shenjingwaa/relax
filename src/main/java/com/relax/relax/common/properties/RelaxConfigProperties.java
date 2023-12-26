package com.relax.relax.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("relax")
public class RelaxConfigProperties {

    /**
     * 实体位置
     */
    private String entityLocations;

    /**
     * 自动创建表是否开启
     */
    private boolean autoCreateTable;
}
