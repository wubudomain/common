package top.wboost.common.boost.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import top.wboost.common.base.annotation.AutoWebApplicationConfig;
import top.wboost.common.boost.handler.BoostHandlerAdapter;
import top.wboost.common.boost.handler.BoostUrlHandlerMapping;
import top.wboost.common.system.spring.converter.ParamMethodArgumentResolver;
import top.wboost.common.utils.web.interfaces.context.EzWebApplicationListener;

/**
 * 快速生成接口配置项
 * @className BoostConfig
 * @author jwSun
 * @date 2017年9月13日 下午5:37:46
 * @version 1.0.0
 */
@Configuration
@AutoWebApplicationConfig("boooooootConfig")
public class BoostConfig implements EzWebApplicationListener {

    @Bean
    public BoostUrlHandlerMapping boostUrlHandlerMapping() {
        BoostUrlHandlerMapping mapping = new BoostUrlHandlerMapping();
        mapping.setOrder(101);//兼容spring-boot 排序在资源simpleUrl之前s);
        return mapping;
    }

    @Bean
    public BoostHandlerAdapter boostHandlerAdapter() {
        return new BoostHandlerAdapter();
    }

    /**
     * 增加自定义converter实现
     */
    @Override
    public void onWebApplicationEvent(ContextRefreshedEvent event) {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = event.getApplicationContext()
                .getBean(RequestMappingHandlerAdapter.class);
        if (requestMappingHandlerAdapter != null) {
            List<HandlerMethodArgumentResolver> resolversAdd = new ArrayList<>();
            Map<String, ParamMethodArgumentResolver> paramMethodArgumentResolver = event.getApplicationContext()
                    .getBeansOfType(ParamMethodArgumentResolver.class, true, false);
            ApplicationContext app = event.getApplicationContext().getParent();
            if (app != null) {
                paramMethodArgumentResolver.putAll(app.getBeansOfType(ParamMethodArgumentResolver.class, true, false));
            }
            paramMethodArgumentResolver.forEach((beanName, bean) -> {
                resolversAdd.add(bean);
            });
            requestMappingHandlerAdapter.setCustomArgumentResolvers(resolversAdd);
            requestMappingHandlerAdapter.setArgumentResolvers(null);
            requestMappingHandlerAdapter.afterPropertiesSet();
        }
    }

}
