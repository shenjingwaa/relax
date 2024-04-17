package com.relax.relax.common.proxy.node.format;

import com.alibaba.fastjson2.JSON;
import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewBeforeProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultEntityFormatProxyNode extends RelaxViewBeforeProxy {
    @Override
    public <T> T proxy(T t, HttpServletRequest request, HttpServletResponse response) {
        return JSON.to((Class<T>) getExecutor().getBaseEntityClass(), JSON.toJSONString(t));
    }

    @Override
    public <T> boolean check(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        return type == ProxyMethodType.ADD ||
                type == ProxyMethodType.UPDATE ||
                type == ProxyMethodType.DELETE ||
                type == ProxyMethodType.PAGE ||
                type == ProxyMethodType.LIST;
    }
}
