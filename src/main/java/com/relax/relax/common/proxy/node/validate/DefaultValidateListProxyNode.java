package com.relax.relax.common.proxy.node.validate;

import com.relax.relax.common.constants.ValidationGroup;
import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewBeforeProxy;
import com.relax.relax.common.proxy.RelaxViewProxy;
import com.relax.relax.common.utils.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultValidateListProxyNode extends RelaxViewBeforeProxy {


    @Override
    public boolean check(Object object, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        return type == ProxyMethodType.LIST;
    }

    @Override
    public <T> T proxy(T t, HttpServletRequest request, HttpServletResponse response) {
        ValidationUtil.validate(t);
        ValidationUtil.validate(t, ValidationGroup.List.class);
        return t;
    }
}
