package com.relax.relax.common.proxy.node;

import com.relax.relax.common.constants.ValidationGroup;
import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewProxy;
import com.relax.relax.common.utils.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultValidateProxyNode extends RelaxViewProxy {


    @Override
    public boolean check(Object object, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        return type == ProxyMethodType.ADD;
    }

    @Override
    public <T> T proxy(T t, HttpServletRequest request, HttpServletResponse response) {
        ValidationUtil.validate(t, ValidationGroup.Add.class);
        return t;
    }
}
