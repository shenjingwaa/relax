package com.relax.relax.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.domain.RelaxResult;
import com.relax.relax.common.enums.SqlType;
import com.relax.relax.common.executor.SqlOperationExecutor;
import com.relax.relax.common.utils.SpringUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("unused")
public class BaseController<T> {

    private final Class<T> baseEntityClass;

    private final ObjectMapper mapper = new ObjectMapper();

    public BaseController(Class<T> baseEntityClass) {
        this.baseEntityClass = baseEntityClass;
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult add(@RequestBody T entity,HttpServletRequest request) {
        T instance = mapper.convertValue(entity, baseEntityClass);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.INSERT, request, instance));
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult update(@RequestBody T entity,HttpServletRequest request) {
        T instance = mapper.convertValue(entity, baseEntityClass);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.UPDATE_BY_ID, request, instance));
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult delete(@RequestBody T entity,HttpServletRequest request) {
        T instance = mapper.convertValue(entity, baseEntityClass);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.DELETE_BY_ID, request, instance));
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult page(@RequestBody T entity, HttpServletRequest request) {
        T instance = mapper.convertValue(entity, baseEntityClass);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.SELECT_PAGE, request, instance));
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult list(@RequestBody T entity, HttpServletRequest request) {
        T instance = mapper.convertValue(entity, baseEntityClass);
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.SELECT_LIST, request, instance));
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public RelaxResult info(HttpServletRequest request) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T instance = baseEntityClass.newInstance();
        String uniqueFieldName = SqlType.getUniqueFieldName(instance);
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
        return RelaxResult.success(SpringUtil.getBean(SqlOperationExecutor.class).submit(SqlType.SELECT_ONE, request, instance));
    }

}
