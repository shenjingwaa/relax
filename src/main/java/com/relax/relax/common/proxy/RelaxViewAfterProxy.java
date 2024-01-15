package com.relax.relax.common.proxy;

import com.relax.relax.common.enums.ProxyMethodType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RelaxViewAfterProxy extends RelaxViewProxy{
    @Override
    public abstract boolean check(Object o, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response);

    @Override
    public abstract Object proxy(Object o, HttpServletRequest request, HttpServletResponse response);
}
