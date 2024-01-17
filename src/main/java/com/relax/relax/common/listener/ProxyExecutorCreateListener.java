package com.relax.relax.common.listener;

import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.common.annotation.RelaxProxy;
import com.relax.relax.common.cofig.RelaxProxyConfiguration;
import com.relax.relax.common.container.EntityHandleContainer;
import com.relax.relax.common.executor.ProxyAfterExecutor;
import com.relax.relax.common.executor.ProxyBeforeExecutor;
import com.relax.relax.common.proxy.RelaxViewProxy;
import com.relax.relax.common.proxy.node.conversion.DefaultInfoConversionProxyNode;
import com.relax.relax.common.proxy.node.format.*;
import com.relax.relax.common.proxy.node.validate.*;
import com.relax.relax.common.utils.RelaxProxyUtil;
import com.relax.relax.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

@Slf4j
public class ProxyExecutorCreateListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationContext context;

    public ProxyExecutorCreateListener(ApplicationContext context) {
        this.context = context;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        context.getBeansWithAnnotation(RelaxClass.class).forEach((beanName, bean) -> {
            loadingBeforeExecutor(bean);
            loadingAfterExecutor(bean);
        });
        registerProxy();
    }

    private static void loadingBeforeExecutor(Object bean) {
        ProxyBeforeExecutor beforeExecutor = new ProxyBeforeExecutor();
        beforeExecutor.setViewClass(bean.getClass());
        beforeExecutor.setBaseEntityClass(EntityHandleContainer.get(bean.getClass()));

        beforeExecutor.addProxy(new DefaultEntityFormatProxyNode());

        beforeExecutor.addProxyAfter(DefaultEntityFormatProxyNode.class, new DefaultValidateAddProxyNode());
        beforeExecutor.addProxyAfter(DefaultEntityFormatProxyNode.class, new DefaultValidateUpdateProxyNode());
        beforeExecutor.addProxyAfter(DefaultEntityFormatProxyNode.class, new DefaultValidateDeleteProxyNode());
        beforeExecutor.addProxyAfter(DefaultEntityFormatProxyNode.class, new DefaultValidateListProxyNode());
        beforeExecutor.addProxyAfter(DefaultEntityFormatProxyNode.class, new DefaultValidatePageProxyNode());
        beforeExecutor.addProxyAfter(DefaultEntityFormatProxyNode.class, new DefaultValidateInfoProxyNode());

        beforeExecutor.addProxyAfter(DefaultValidateInfoProxyNode.class, new DefaultInfoConversionProxyNode());

        SpringUtil.addBean(beforeExecutor, bean.getClass().getName() + "_" + ProxyBeforeExecutor.class.getSimpleName());
    }

    private static void loadingAfterExecutor(Object bean) {
        ProxyAfterExecutor afterExecutor = new ProxyAfterExecutor();
        afterExecutor.setViewClass(bean.getClass());
        afterExecutor.setBaseEntityClass(EntityHandleContainer.get(bean.getClass()));

        afterExecutor.addProxy(new DefaultPageFormatProxyNode());
        afterExecutor.addProxy(new DefaultListFormatProxyNode());
        afterExecutor.addProxy(new DefaultInfoFormatProxyNode());
        afterExecutor.addProxy(new DefaultResultFormatProxyNode());
        SpringUtil.addBean(afterExecutor, bean.getClass().getName() + "_" + ProxyAfterExecutor.class.getSimpleName());
    }

    private void registerProxy() {
        try {
            SpringUtil.getBean(RelaxProxyConfiguration.class).registerProxy();
        } catch (Exception ignored) {
        }
        register();
    }

    private void register() {
        context.getBeansWithAnnotation(RelaxProxy.class).forEach((beanName, bean) -> {
            RelaxProxy proxy = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(RelaxProxy.class);
            if (proxy.afterClass() != RelaxViewProxy.class) {
                RelaxProxyUtil.addProxyAfter(proxy.relaxClass(), proxy.afterClass(), (RelaxViewProxy) bean, proxy.proxyType());
            } else {
                RelaxProxyUtil.addProxy(proxy.relaxClass(), (RelaxViewProxy) bean, proxy.proxyType());
            }
        });
    }

}
