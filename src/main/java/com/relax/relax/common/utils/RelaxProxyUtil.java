package com.relax.relax.common.utils;

import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.common.enums.ProxyType;
import com.relax.relax.common.exceptions.proxy.ProxyInjectionException;
import com.relax.relax.common.executor.ProxyAfterExecutor;
import com.relax.relax.common.executor.ProxyBeforeExecutor;
import com.relax.relax.common.executor.ProxyExecutor;
import com.relax.relax.common.proxy.RelaxViewProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RelaxProxyUtil {
    public static void addProxyBefore(Class<?> relaxClass, Class<? extends RelaxViewProxy> beaforeClass, RelaxViewProxy relaxProxy, ProxyType proxyType) {
        if (!checkAnnotation(relaxClass, relaxProxy)) return;
        try {
            ProxyExecutor executor;
            if (proxyType == ProxyType.BEFORE) {
                executor = getProxyBeforeExecutorBean(relaxClass);
                executor.addProxyBefore(beaforeClass, relaxProxy);
            }
            if (proxyType == ProxyType.AFTER) {
                executor = getProxyAfterExecutorBean(relaxClass);
                executor.addProxyBefore(beaforeClass, relaxProxy);
            }
        } catch (Exception e) {
            throw new ProxyInjectionException("Serious abnormality As Injection Proxy Node Unknown exception occurred.");
        }
    }

    public static void addProxyAfter(Class<?> relaxClass, Class<? extends RelaxViewProxy> afterClass, RelaxViewProxy relaxProxy, ProxyType proxyType) {
        if (!checkAnnotation(relaxClass, relaxProxy)) return;
        try {
            ProxyExecutor executor;
            if (proxyType == ProxyType.BEFORE) {
                executor = getProxyBeforeExecutorBean(relaxClass);
                executor.addProxyAfter(afterClass, relaxProxy);
            }
            if (proxyType == ProxyType.AFTER) {
                executor = getProxyAfterExecutorBean(relaxClass);
                executor.addProxyAfter(afterClass, relaxProxy);
            }
        } catch (Exception e) {
            throw new ProxyInjectionException("Serious abnormality As Injection Proxy Node Unknown exception occurred.");
        }
    }

    public static void addProxy(Class<?> relaxClass, RelaxViewProxy relaxProxy, ProxyType proxyType) {
        if (!checkAnnotation(relaxClass, relaxProxy)) return;
        try {
            ProxyExecutor executor;
            if (proxyType == ProxyType.BEFORE) {
                executor = getProxyBeforeExecutorBean(relaxClass);
                executor.addProxy(relaxProxy);
            }
            if (proxyType == ProxyType.AFTER) {
                executor = getProxyAfterExecutorBean(relaxClass);
                executor.addProxy(relaxProxy);
            }
        } catch (Exception e) {
            throw new ProxyInjectionException("Serious abnormality As Injection Proxy Node Unknown exception occurred.");
        }
    }

    public static ProxyBeforeExecutor getProxyBeforeExecutorBean(Class<?> relaxClass) {
        return (ProxyBeforeExecutor) SpringUtil.getBean(relaxClass.getName() + "_" + ProxyBeforeExecutor.class.getSimpleName());
    }

    public static ProxyAfterExecutor getProxyAfterExecutorBean(Class<?> relaxClass) {
        return (ProxyAfterExecutor) SpringUtil.getBean(relaxClass.getName() + "_" + ProxyAfterExecutor.class.getSimpleName());
    }

    private static Boolean checkAnnotation(Class<?> relaxClass, RelaxViewProxy relaxProxy) {
        if (!relaxClass.isAnnotationPresent(RelaxClass.class)) {
            log.warn("[relax] @RelaxProxy annotation identification class " + relaxClass.getSimpleName() + " not have @RelaxClass annotation, " + "skip loading " + relaxProxy.getClass().getSimpleName() + " proxy node");
            return false;
        }
        return true;
    }
}
