package com.relax.relax.common.executor;

import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewProxy;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public abstract class ProxyExecutor {
    List<RelaxViewProxy> proxyContainer = new ArrayList<>();

    @Getter
    @Setter
    private Class<?> viewClass;

    @Getter
    @Setter
    private Class<?> baseEntityClass;

    public void addProxyBefore(Class<? extends RelaxViewProxy> clazz, RelaxViewProxy proxy) {
        proxy.setExecutor(this);
        if (proxyContainer.contains(proxy)) return;
        int index = getProxyIndex(clazz);
        if (index >= 0) {
            proxyContainer.add(index, proxy);
        } else {
            proxyContainer.add(proxy);
        }
    }

    public void addProxyAfter(Class<? extends RelaxViewProxy> clazz, RelaxViewProxy proxy) {
        proxy.setExecutor(this);
        if (proxyContainer.contains(proxy)) return;
        int index = getProxyIndex(clazz);
        if (index >= 0) {
            proxyContainer.add(index + 1, proxy);
        } else {
            proxyContainer.add(proxy);
        }
    }

    public void addProxy(RelaxViewProxy proxy) {
        proxy.setExecutor(this);
        if (proxyContainer.contains(proxy)) return;
        proxyContainer.add(proxy);
    }

    public int getProxyIndex(Class<? extends RelaxViewProxy> clazz) {
        for (int i = 0; i < proxyContainer.size(); i++) {
            if (proxyContainer.get(i).getClass() == clazz) {
                return i;
            }
        }
        return -1;
    }

    abstract <T> T run(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response);
}
