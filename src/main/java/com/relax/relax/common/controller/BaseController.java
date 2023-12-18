package com.relax.relax.common.controller;

import cn.hutool.core.bean.BeanUtil;
import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.domain.RelaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@AllArgsConstructor
public class BaseController<T> {

    private final Class<T> baseEntityClass;

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult add(@RequestBody T entity){
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute add method for {} fail," +
                            "requestBody is {}\n" +
                            "and the fail reason is :{}",
                    this.baseEntityClass.getName(),
                    entity,
                    e.getMessage());
        }
        log.debug("execute add method success,requestBody is {}", entity);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult update(@RequestBody T entity){
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute update method for {} fail," +
                            "requestBody is {}\n" +
                            "and the fail reason is :{}",
                    this.baseEntityClass.getName(),
                    entity,
                    e.getMessage());
        }
        log.debug("execute update method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult delete(@RequestBody T entity){
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute delete method for {} fail," +
                            "requestBody is {}\n" +
                            "and the fail reason is :{}",
                    this.baseEntityClass.getName(),
                    entity,
                    e.getMessage());
        }
        log.debug("execute delete method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public RelaxResult info(@RequestParam String id){
        log.debug("execute info method ,RequestParam is id = {}",id);
        return RelaxResult.success();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult page(@RequestBody T entity){
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute page method for {} fail," +
                            "requestBody is {}\n" +
                            "and the fail reason is :{}",
                    this.baseEntityClass.getName(),
                    entity,
                    e.getMessage());
        }
        log.debug("execute page method ,requestBody is {}",entity);
        return RelaxResult.success();
    }

}
