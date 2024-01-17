package com.relax.relax.common.proxy.node.format;

import com.relax.relax.common.domain.RelaxResult;
import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewAfterProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultResultFormatProxyNode extends RelaxViewAfterProxy {
    @Override
    public boolean check(Object o, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public Object proxy(Object o, HttpServletRequest request, HttpServletResponse response) {
        return RelaxResult.success(o);
    }
}
