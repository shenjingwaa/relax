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

    private Object data;

    public static RelaxResult success(){
        return new RelaxResult("操作成功", 200, null);
    }

    public static RelaxResult fail(){
        return new RelaxResult("操作失败", 500, "服务器异常,请联系管理员");
    }

    public static RelaxResult success(Object data) {
        return new RelaxResult("操作成功", 200, data);
    }

}
