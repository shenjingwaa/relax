package com.relax.relax.common.controller;

import com.relax.relax.common.domain.RelaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public class BaseController<T> {

    @ResponseBody
    public RelaxResult info(){
        log.info("show info");
        return RelaxResult.success();
    }
}
