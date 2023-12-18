package com.relax.relax.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("relax")
public class RelaxConfigProperties {
    private String entityLocations;
}
