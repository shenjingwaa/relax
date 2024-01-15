package com.relax.relax.common.executor;

import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyAfterExecutor implements ProxyExecutor {

    @Override
    public Object run(Object t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        Object executeResult = t;
        for (RelaxViewProxy proxy : proxyContainer) {
            executeResult = proxy.execute(executeResult, type, request, response);
        }
        return executeResult;
    }
}
