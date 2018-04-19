package top.wboost.common.context.config;

import org.springframework.context.config.ContextNamespaceHandler;

/**
 * 扩展spring-context handler
 * @className ContextNamespaceHandlerSupports
 * @author jwSun
 * @date 2017年12月5日 下午7:00:47
 * @version 1.0.0
 */
public class ContextNamespaceHandlerSupports extends ContextNamespaceHandler {

    @Override
    public void init() {
        // super.init();
        // 原生扫描器
        registerBeanDefinitionParser("component-scan", new SupportsComponentScanBeanDefinitionParser());
        // mvc配置文件中使用
        registerBeanDefinitionParser("web-component-scan", new WebSupportsComponentScanBeanDefinitionParser());
        // service配置文件中使用
        registerBeanDefinitionParser("root-component-scan", new RootSupportsComponentScanBeanDefinitionParser());
        // 代理配置文件中使用
        registerBeanDefinitionParser("proxy-component-scan", new ProxySupportsComponentScanBeanDefinitionParser());
        // registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
    }

}
