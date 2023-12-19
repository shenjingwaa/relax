package com.relax.relax.common.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.domain.RelaxResult;
import com.relax.relax.common.factory.BaseSqlEnum;
import com.relax.relax.common.factory.SqlOperationFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("unused")
@Slf4j
@AllArgsConstructor
public class BaseController<T> {

    private final Class<T> baseEntityClass;

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult add(@RequestBody T entity,HttpServletRequest request) {
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
            log.debug("execute add method success,requestBody is {}", entity);
            return RelaxResult.success(
                    SpringUtil.getBean(SqlOperationFactory.class).submit(BaseSqlEnum.INSERT,request,instance)
            );
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute add method for {} fail,requestBody is {}\nand the fail reason is :{}",
                    this.baseEntityClass.getName(), entity, e.getMessage());
        }
        return RelaxResult.fail();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult update(@RequestBody T entity,HttpServletRequest request) {
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
            log.debug("execute update method ,requestBody is {}", entity);
            return RelaxResult.success(
                    SpringUtil.getBean(SqlOperationFactory.class).submit(BaseSqlEnum.UPDATE_BY_ID,request,instance)
            );
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute update method for {} fail,requestBody is {}\nand the fail reason is :{}",
                    this.baseEntityClass.getName(), entity, e.getMessage());
        }
        return RelaxResult.fail();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult delete(@RequestBody T entity,HttpServletRequest request) {
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
            log.debug("execute delete method ,requestBody is {}", entity);
            return RelaxResult.success(
                    SpringUtil.getBean(SqlOperationFactory.class).submit(BaseSqlEnum.DELETE_BY_ID,request,instance)
            );
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute delete method for {} fail,requestBody is {}\nand the fail reason is :{}",
                    this.baseEntityClass.getName(), entity, e.getMessage());
        }
        return RelaxResult.fail();
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public RelaxResult info(HttpServletRequest request) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T instance = baseEntityClass.newInstance();
        String uniqueFieldName = BaseSqlEnum.getUniqueFieldName(instance);
        String parameter = request.getParameter(uniqueFieldName);

        Field field = baseEntityClass.getDeclaredField(uniqueFieldName);
        field.setAccessible(true);
        if (Objects.equals(field.getType(), String.class)) {
            field.set(instance, parameter);
        } else if (Objects.equals(field.getType(), Long.class)) {
            field.set(instance, Long.parseLong(parameter));
        } else if (Objects.equals(field.getType(), Integer.class)) {
            field.set(instance, Integer.parseInt(parameter));
        } else if (Objects.equals(field.getType(), Short.class)) {
            field.set(instance, Short.parseShort(parameter));
        } else {
            throw new IllegalArgumentException("args format error.");
        }
        log.debug("execute info method ,RequestParam is id = {}", instance);
        return RelaxResult.success(
                SpringUtil.getBean(SqlOperationFactory.class).submit(BaseSqlEnum.SELECT_ONE,request,instance)
        );
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult page(@RequestBody T entity,HttpServletRequest request) {
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
            return RelaxResult.success(BaseSqlEnum.SELECT_PAGE.execute(instance,request));
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute page method for {} fail,requestBody is {}\n and the fail reason is :{}",
                    this.baseEntityClass.getName(), entity, e.getMessage());
        }
        return RelaxResult.fail();
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult list(@RequestBody T entity,HttpServletRequest request) {
        try {
            T instance = baseEntityClass.newInstance();
            BeanUtil.copyProperties(entity, instance);
            return RelaxResult.success(BaseSqlEnum.SELECT_LIST.execute(instance,request));
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("execute list method for {} fail,requestBody is {}\nand the fail reason is :{}",
                    this.baseEntityClass.getName(), entity, e.getMessage());
        }
        return RelaxResult.fail();
    }

}
