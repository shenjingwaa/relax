package com.relax.relax.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class RelaxResult {

    private String msg;

    private int code;

    public static RelaxResult success(){
        return new RelaxResult("操作成功",200);
    }

}
