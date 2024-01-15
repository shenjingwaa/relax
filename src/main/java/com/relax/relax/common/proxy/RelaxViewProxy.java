package com.relax.relax.common.proxy;

import com.relax.relax.common.enums.ProxyMethodType;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class RelaxViewProxy {

    public abstract <T> T proxy(T t, HttpServletRequest request, HttpServletResponse response);

    public abstract <T> boolean check(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response);

    public <T> T execute(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        if (this.check(t, type, request, response)) {
            return proxy(t, request, response);
        }
        return t;
    }

}
