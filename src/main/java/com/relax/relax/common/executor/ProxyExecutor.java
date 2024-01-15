package com.relax.relax.common.executor;

import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface ProxyExecutor {
    List<RelaxViewProxy> proxyContainer = new ArrayList<>();


    default void addProxyBefore(Class<? extends RelaxViewProxy> clazz, RelaxViewProxy proxy) {
        int index = getProxyIndex(clazz);
        if (index >= 0) {
            proxyContainer.add(index, proxy);
        } else {
            proxyContainer.add(proxy);
        }
    }

    default int getProxyIndex(Class<? extends RelaxViewProxy> clazz) {
        for (int i = 0; i < proxyContainer.size(); i++) {
            if (proxyContainer.get(i).getClass() == clazz) {
                return i;
            }
        }
        return -1;
    }

    <T> T run(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response);
}
