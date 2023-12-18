package com.relax.relax.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 26512
 * @email 2651229241@qq.com
 * @description: mybatisPlus配置类
 * @time 2022/11/28 19:01
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 分页插件
     * @return interceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor=new PaginationInnerInterceptor();
        innerInterceptor.setDbType(DbType.MYSQL);
        innerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }
}
