package com.relax.relax.domain;

import com.relax.relax.common.annotation.RelaxColumn;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.annotation.RelaxId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@RelaxEntity(tableName = "relax_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

//    @RelaxId
    @RelaxColumn
    private Long id;

    @RelaxColumn
    private String userName;

    @RelaxColumn
    private Integer age;

    @RelaxColumn
    private LocalDateTime birthday;

}
