package com.relax.relax.common.handler;

import com.relax.relax.common.annotation.EnableRelax;
import com.relax.relax.common.annotation.MappingType;
import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.common.controller.BaseController;
import com.relax.relax.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class BaseMappingHandler implements InitializingBean , ApplicationContextAware {

    private final ApplicationContext context;

    public BaseMappingHandler(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() {
        init();
    }

    public void init() {
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        context.getBeansWithAnnotation(SpringBootApplication.class)
                .forEach((beanName, bean) -> {
                    EnableRelax enableRelax = bean.getClass().getAnnotation(EnableRelax.class);
                    if (Objects.nonNull(enableRelax) && enableRelax.isEnable()) {
                        log.info("[relax] start to scan crud annotation.");
                        context.getBeansWithAnnotation(RelaxClass.class)
                                .forEach((relaxBeanName, relaxBean) -> {
                                    RelaxClass relaxClass = relaxBean.getClass().getAnnotation(RelaxClass.class);
                            for (Method method : BaseController.class.getMethods()) {
                                // 获取用户选择自动映射的接口
                                List<String> targetMethod = Arrays.stream(relaxClass.methods()).collect(Collectors.toList());
                                //检查具有@MappingType注解的方法并且过滤不需要的接口
                                if (method.isAnnotationPresent(MappingType.class) && targetMethod.contains(method.getName())) {
                                    //构建映射信息
                                    String prefix = relaxClass.prefix();
                                    if (prefix.startsWith("/")){
                                        prefix = prefix.replaceFirst("/","");
                                    }
                                    RequestMappingInfo mapping = RequestMappingInfo
                                            .paths(String.format("/%s/%s", prefix, method.getName()))
                                            .options(initConfiguration(handlerMapping))
                                            .methods(method.getAnnotation(MappingType.class).value())
                                            .build();

                                    BaseController controller = new BaseController(relaxClass.entityType());
                                    //注册映射信息
                                    handlerMapping.registerMapping(mapping, controller, method);
                                }
                            }
                                });
            }
                    log.info("[relax] Base api init success.");
                });

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


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.setContext(applicationContext);
    }
}
