package com.relax.relax.common.handler;

import com.relax.relax.common.annotation.EnableRelax;
import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.common.controller.BaseController;
import com.relax.relax.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class BaseMappingHandler implements ApplicationContextAware, ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationContext context;

    public BaseMappingHandler(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.setContext(applicationContext);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Class<?> mainApplicationClass = event.getSpringApplication().getMainApplicationClass();
        EnableRelax enableRelax = mainApplicationClass.getAnnotation(EnableRelax.class);
        if (Objects.nonNull(enableRelax) && enableRelax.isEnable()) {
            log.info("[relax] start to scan crud annotation.");
            startToRegisterApi(handlerMapping);
            log.info("[relax] Base api init success.");
        }
    }

    /**
     * 通过获取注解开始准备注册api
     */
    private void startToRegisterApi(RequestMappingHandlerMapping handlerMapping) {
        context.getBeansWithAnnotation(RelaxClass.class)
                .forEach((relaxBeanName, relaxBean) -> {
                    Class<?> beanClass = relaxBean.getClass();
                    RelaxClass relaxClass = beanClass.getAnnotation(RelaxClass.class);
                    RequestMapping requestMapping = beanClass.getDeclaredAnnotation(RequestMapping.class);
                    if ((Objects.isNull(requestMapping) || requestMapping.value().length==0) &&
                            (relaxClass.prefix().length==0)){
                        log.error("[relax] Your controller must identify the path prefix in the @RelaxClass and @RequestMapping for class {}.",beanClass.getName());
                        return;
                    }
                    for (Method method : BaseController.class.getMethods()) {
                        registerApiForMethod(relaxBean, method, relaxClass, handlerMapping);
                    }
                });
    }

    /**
     * 通过各个通用方法执行api注册
     */
    private void registerApiForMethod(Object relaxBean, Method method, RelaxClass relaxClass, RequestMappingHandlerMapping handlerMapping) {
        // 获取用户选择自动映射的接口
        List<String> targetMethod = Arrays.stream(relaxClass.methods()).collect(Collectors.toList());
        //检查具有@MappingType注解的方法并且过滤不需要的接口
        if (method.isAnnotationPresent(MappingType.class) && targetMethod.contains(method.getName())) {
            //构建映射信息
            RequestMapping requestMapping = relaxBean.getClass().getDeclaredAnnotation(RequestMapping.class);
            String[] prefixs = null;
            if (Objects.nonNull(requestMapping) && requestMapping.value().length > 0) {
                prefixs = requestMapping.value();
            } else {
                String[] classPrefix = relaxClass.prefix();
                if (Objects.isNull(classPrefix) || classPrefix.length == 0) {
                    return;
                }
                prefixs = classPrefix;
            }
            for (String prefix : prefixs) {
                doRegister(method, prefix, handlerMapping, relaxClass);
            }
        }
    }

    private void doRegister(Method method, String prefix, RequestMappingHandlerMapping handlerMapping, RelaxClass relaxClass) {
        if (prefix.startsWith("/")) {
            prefix = prefix.replaceFirst("/", "");
        }
        RequestMappingInfo mapping = RequestMappingInfo
                .paths(String.format("/%s/%s", prefix, method.getName()))
                .options(initConfiguration(handlerMapping))
                .methods(method.getAnnotation(MappingType.class).value())
                .build();
        log.debug("[relax] Do register api : {}", String.format("/%s/%s", prefix, method.getName()));

        //注册映射信息
        handlerMapping.registerMapping(mapping, new BaseController(relaxClass.entityType()), method);
    }

    /**
     * 初始化请求映射配置
     */
    @SuppressWarnings("deprecation")
    private RequestMappingInfo.BuilderConfiguration initConfiguration(RequestMappingHandlerMapping handlerMapping) {
        RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
        config.setTrailingSlashMatch(handlerMapping.useTrailingSlashMatch());
        config.setContentNegotiationManager(handlerMapping.getContentNegotiationManager());
        if (handlerMapping.getPatternParser() != null) {
            config.setPatternParser(handlerMapping.getPatternParser());
            Assert.isTrue(!handlerMapping.useSuffixPatternMatch() && !handlerMapping.useRegisteredSuffixPatternMatch(),
                    "Suffix pattern matching not supported with PathPatternParser.");
        } else {
            config.setSuffixPatternMatch(handlerMapping.useSuffixPatternMatch());
            config.setRegisteredSuffixPatternMatch(handlerMapping.useRegisteredSuffixPatternMatch());
            config.setPathMatcher(handlerMapping.getPathMatcher());
        }
        return config;
    }

}
