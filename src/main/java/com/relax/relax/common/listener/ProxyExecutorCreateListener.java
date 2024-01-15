package com.relax.relax.common.listener;

import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.common.cofig.RelaxProxyConfiguration;
import com.relax.relax.common.executor.ProxyAfterExecutor;
import com.relax.relax.common.executor.ProxyBeforeExecutor;
import com.relax.relax.common.proxy.RelaxViewProxy;
import com.relax.relax.common.proxy.node.DefaultValidateProxyNode;
import com.relax.relax.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
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
            ProxyBeforeExecutor beforeExecutor = new ProxyBeforeExecutor();
            beforeExecutor.addProxyBefore((Class<? extends RelaxViewProxy>) bean.getClass(), new DefaultValidateProxyNode());
            SpringUtil.addBean(beforeExecutor, bean.getClass().getName() + "_" + ProxyBeforeExecutor.class.getSimpleName());

            ProxyAfterExecutor afterExecutor = new ProxyAfterExecutor();
            SpringUtil.addBean(afterExecutor, bean.getClass().getName() + "_" + ProxyAfterExecutor.class.getSimpleName());

            try {
                SpringUtil.getBean(RelaxProxyConfiguration.class).registerProxy();
            } catch (Exception ignored) {
            }
        });
    }
}
