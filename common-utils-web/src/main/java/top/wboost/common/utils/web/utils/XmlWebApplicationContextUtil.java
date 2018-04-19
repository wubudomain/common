package top.wboost.common.utils.web.utils;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.XmlWebApplicationContext;

import top.wboost.common.base.annotation.AutoWebApplicationConfig;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.util.CollectionUtil;

/**
 * mvc文件中配置，获得mvc中的bean
 * @className XmlWebApplicationContextUtil
 * @author jwSun
 * @date 2017年7月7日 上午11:34:44
 * @version 1.0.0
 */
@AutoWebApplicationConfig
public class XmlWebApplicationContextUtil implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static Logger log = LoggerUtil.getLogger(XmlWebApplicationContextUtil.class);

    private static ApplicationContext applicationContext;

    public static Object getBean(String beanId) {
        try {
            Object bean = applicationContext.getBean(beanId);
            return bean;
        } catch (Exception e) {
            log.warn("mvc spring bean :{} cant find.", beanId);
            return null;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            log.warn("mvc spring bean :{} cant find.", clazz.getName());
            return null;
        }
    }

    public static ApplicationContext getApplicationContext() {
        try {
            return applicationContext;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
        log.info("init");
    }

    /**
     * 在spring容器中增加bean(在此增加的无法注入)
     * @param beanId
     * @param clazz
     * @param properties
     */
    public static void setBean(String beanId, Class<?> clazz, Map<String, Object> properties) {
        ApplicationContext applicationContext = getApplicationContext();
        @SuppressWarnings("resource")
        XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) applicationContext;
        BeanFactory beanFactory = xmlWebApplicationContext.getBeanFactory();
        if (xmlWebApplicationContext.getBeanFactory() instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            if (!CollectionUtil.isEmpty(properties)) {
                properties.forEach((name, value) -> {
                    builder.addPropertyValue(name, value);
                });
            }
            registry.registerBeanDefinition(beanId, builder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 使用BeanFactoryPostProcessor接口,在PostProcessorRegistrationDelegate初始化时,ApplicationContextAware 执行顺序将提前
    }

}
