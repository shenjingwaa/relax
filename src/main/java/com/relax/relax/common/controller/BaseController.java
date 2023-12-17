package com.relax.relax.common.controller;

import com.relax.relax.common.domain.RelaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public class BaseController<T> {

    @PostMapping("/add")
    @ResponseBody
    public RelaxResult add(@RequestBody T entity){
        log.info("execute add method ,requestBody is {}",entity);
        return RelaxResult.success();
    }
}
