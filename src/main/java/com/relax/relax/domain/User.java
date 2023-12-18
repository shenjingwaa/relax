package com.relax.relax.domain;

import com.relax.relax.common.annotation.RelaxEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@RelaxEntity(tableName = "relax_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

}
