package com.relax.relax.common.executor;

import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyBeforeExecutor extends ProxyExecutor {
    @Override
    public <T> T run(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        {
            T executeResult = t;
            for (RelaxViewProxy proxy : proxyContainer) {
                executeResult = proxy.execute(executeResult, type, request, response);
            }
            return executeResult;
        }
    }
}
