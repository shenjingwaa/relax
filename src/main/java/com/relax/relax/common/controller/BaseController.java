package com.relax.relax.common.controller;

import com.alibaba.fastjson2.JSON;
import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.constants.ValidationGroup;
import com.relax.relax.common.domain.RelaxResult;
import com.relax.relax.common.enums.SqlType;
import com.relax.relax.common.executor.SqlOperationExecutor;
import com.relax.relax.common.utils.BeanUtil;
import com.relax.relax.common.utils.SpringUtil;
import com.relax.relax.common.utils.ValidationUtil;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class BaseController<T> {

    private final Class<T> baseEntityClass;

    public BaseController(Class<T> baseEntityClass) {
        this.baseEntityClass = baseEntityClass;
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult add(@RequestBody T entity, HttpServletRequest request) {
        T instance = JSON.to(baseEntityClass, entity);
        ValidationUtil.validate(instance, ValidationGroup.Add.class);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.INSERT, request, instance,baseEntityClass));
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult update(@RequestBody T entity,HttpServletRequest request) {
        T instance = JSON.to(baseEntityClass, entity);
        ValidationUtil.validate(instance, ValidationGroup.Update.class);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.UPDATE_BY_ID, request, instance,baseEntityClass));
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult delete(@RequestBody T entity,HttpServletRequest request) {
        T instance = JSON.to(baseEntityClass, entity);
        ValidationUtil.validate(instance, ValidationGroup.Delete.class);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.DELETE_BY_ID, request, instance,baseEntityClass));
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult page(@RequestBody T entity, HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        T instance = JSON.to(baseEntityClass, entity);
        ValidationUtil.validate(instance, ValidationGroup.Page.class);
        Map<String, Object> result = SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.SELECT_PAGE, request, instance, baseEntityClass);
        List<Map<String, Object>> page = (List<Map<String, Object>>) result.get("page");
        result.put("page", page.stream()
                .map(map -> BeanUtil.mapToBean(BeanUtil.mapKVToCamelCase(map), baseEntityClass, true))
                .collect(Collectors.toList()));
        return RelaxResult.success(result);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult list(@RequestBody T entity, HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        T instance = JSON.to(baseEntityClass, entity);
        ValidationUtil.validate(instance, ValidationGroup.List.class);
        Map<String, Object> result = SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.SELECT_LIST, request, instance, baseEntityClass);
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        result.put("list", list.stream()
                .map(map -> BeanUtil.mapToBean(BeanUtil.mapKVToCamelCase(map), baseEntityClass, true))
                .collect(Collectors.toList()));
        return RelaxResult.success(result);
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public RelaxResult info(HttpServletRequest request) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T instance = baseEntityClass.newInstance();
        String uniqueFieldName = SqlType.getUniqueFieldName(instance);
        String parameter = request.getParameter(uniqueFieldName);
        Assert.isTrue(Objects.nonNull(parameter) && !parameter.isEmpty(),"唯一标识不能为空!");

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
        Map<String, Object> result = SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.SELECT_BY_ID, request, instance, baseEntityClass);
        T finalResult = BeanUtil.mapToBean(BeanUtil.mapKVToCamelCase((Map<String, Object>) result.get("info")), baseEntityClass, true);
        result.put("info", finalResult);
        return RelaxResult.success(finalResult);
    }

}
