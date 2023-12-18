package com.relax.relax.common.handler;

import cn.hutool.core.util.ClassUtil;
import com.relax.relax.common.annotation.EnableRelax;
import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.common.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Configuration
public class BaseMappingHandler {

    private final ApplicationContext context;

    public BaseMappingHandler(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() throws NoSuchMethodException {
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);

        for (Class<?> baseClass : ClassUtil.scanPackage()) {
            if (baseClass.isAnnotationPresent(SpringBootApplication.class)) {
                EnableRelax enable = baseClass.getAnnotation(EnableRelax.class);
                if (Objects.nonNull(enable)) {
                    log.info("start to scan crud annotation.");

                    for (Class<?> classItem : ClassUtil.scanPackage(ClassUtil.getPackage(baseClass))) {
                        if (classItem.isAnnotationPresent(RelaxClass.class)) {
                            log.info("need to auto crud class is {}", classItem.getName());
                            String[] mappingMethods = classItem.getAnnotation(RelaxClass.class).methods();
                            // TODO: 2023/12/17 0017 将动态拼接路径移动到该判断内部
                        }
                    }
                }
                break;
            }
        }
        // TODO: 2023/12/17 0017 动态拼接路径
        RequestMappingInfo mapping = RequestMappingInfo
                .paths("/user/add")
                .options(initConfiguration(handlerMapping))
                .methods(RequestMethod.POST)
                .build();

        BaseController<Object> controller = new BaseController<>();
        Method add = BaseController.class.getMethod("add", Object.class);
        handlerMapping.registerMapping(mapping, controller, add);

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
