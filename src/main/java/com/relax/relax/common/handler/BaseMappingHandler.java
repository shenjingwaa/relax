package com.relax.relax.common.handler;

import cn.hutool.core.util.ClassUtil;
import com.relax.relax.common.controller.BaseController;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
@Configuration
public class BaseMappingHandler {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationArguments applicationArguments;


    @PostConstruct
    public void init() throws NoSuchMethodException {
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        String startClassPath;
        for (Class<?> baseClass : ClassUtil.scanPackage()) {
            if (baseClass.isAnnotationPresent(SpringBootApplication.class)){
                startClassPath = baseClass.getPackageName();
                break;
            }
        }


        RequestMappingInfo mapping = RequestMappingInfo
                .paths("/test/info")
                .options(initConfiguration(handlerMapping))
                .methods(RequestMethod.GET)
                .build();

        BaseController<Object> controller = new BaseController<>();
        Method info = BaseController.class.getMethod("info");
        handlerMapping.registerMapping(mapping, controller, info);

        log.info("init success.");
    }

    /**
     * 初始化请求映射配置
     */
    @SuppressWarnings("deprecation")
    public RequestMappingInfo.BuilderConfiguration initConfiguration(RequestMappingHandlerMapping handlerMapping) {
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
