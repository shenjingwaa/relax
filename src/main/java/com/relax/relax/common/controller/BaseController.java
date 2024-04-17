package com.relax.relax.common.controller;

import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.container.EntityHandleContainer;
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
    public Object add(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.ADD);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.UPDATE);
    }

    @MappingType(RequestMethod.POST)
    @ResponseBody
    public Object delete(@RequestBody T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.DELETE);
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public Object page(T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.PAGE);
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public Object list(T entity, HttpServletRequest request, HttpServletResponse response) {
        return performCrudOperation(entity, request, response, CrudOperationType.LIST);
    }

    @MappingType(RequestMethod.GET)
    @ResponseBody
    public Object info(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        return performCrudOperation(baseEntityClass.newInstance(), request, response, CrudOperationType.INFO);
    }

    private Object performCrudOperation(T entity, HttpServletRequest request, HttpServletResponse response, CrudOperationType operation) {
        T instance = RelaxProxyUtil.getProxyBeforeExecutorBean(viewClass).run(entity, operation.getProxyMethodType(), request, response);
        Map<String, Object> submit = SpringUtil.getBean(SqlOperationExecutor.class).submit(operation.getSqlType(), request, instance, baseEntityClass);
        return RelaxProxyUtil.getProxyAfterExecutorBean(viewClass).run(submit, operation.getProxyMethodType(), request, response);
    }

}
