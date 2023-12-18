package com.relax.relax.domain;

import com.relax.relax.common.annotation.RelaxEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@RelaxEntity(tableName = "relax_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private Integer age;

    private LocalDateTime birthday;

}
