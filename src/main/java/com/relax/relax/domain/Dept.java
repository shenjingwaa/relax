package com.relax.relax.domain;

import com.relax.relax.common.annotation.RelaxColumn;
import com.relax.relax.common.annotation.RelaxEntity;
import com.relax.relax.common.annotation.RelaxId;
import lombok.Data;

@Data
@RelaxEntity(tableName = "relax_dept")
public class Dept {

    @RelaxId
    @RelaxColumn
    private Long id;

    @RelaxColumn
    private String name;
}
