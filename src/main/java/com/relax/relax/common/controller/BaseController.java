package com.relax.relax.common.controller;

import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.container.EntityHandleContainer;
import com.relax.relax.common.domain.RelaxResult;
import com.relax.relax.common.enums.CrudOperationType;
import com.relax.relax.common.executor.SqlOperationExecutor;
import com.relax.relax.common.utils.RelaxProxyUtil;
import com.relax.relax.common.utils.SpringUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@SuppressWarnings("unused")
public class BaseController<T> {

    private final Class<T> baseEntityClass;

    private final Class<?> viewClass;

    public BaseController(Class<T> baseEntityClass, Class<?> viewClass) {
        this.baseEntityClass = baseEntityClass;
        this.viewClass = viewClass;
        EntityHandleContainer.mapping(viewClass, baseEntityClass);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult add(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.ADD);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult update(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.UPDATE);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult delete(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.DELETE);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult page(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.PAGE);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public RelaxResult list(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.LIST);
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public RelaxResult info(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        return performCrudOperation(baseEntityClass.newInstance(), request, response, CrudOperationType.INFO);
    }

    public RelaxResult performCrudOperation(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response, CrudOperationType operation) {
        T instance = RelaxProxyUtil.getProxyBeforeExecutorBean(viewClass).run(entity, operation.getProxyMethodType(), request, response);
        Map<String, Object> submit = SpringUtil.getBean(SqlOperationExecutor.class).submit(operation.getSqlType(), request, instance, baseEntityClass);
        Object result = RelaxProxyUtil.getProxyAfterExecutorBean(viewClass).run(submit, operation.getProxyMethodType(), request, response);
        return RelaxResult.success(result);
    }

}
