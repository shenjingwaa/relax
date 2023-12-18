package com.relax.relax.common.controller;

import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.domain.RelaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
public class BaseController<T> {

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult add(@RequestBody T entity){
        log.info("execute add method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult update(@RequestBody T entity){
        log.info("execute add method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult delete(@RequestBody T entity){
        log.info("execute add method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public RelaxResult info(@RequestBody T entity){
        log.info("execute add method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult page(@RequestBody T entity){
        log.info("execute add method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

}
