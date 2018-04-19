package top.wboost.common.utils.web.utils;

import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import top.wboost.common.exception.BaseException;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.util.CollectionUtil;

public class SpringBeanRegisterUtil {

    private static Logger log = LoggerUtil.getLogger(SpringBeanRegisterUtil.class);

    /**
     * 注册单例对象
     * @param beanId
     * @param clazz
     * @param properties
     * @param beanFactory
     */
    @Deprecated
    public static void registerSingleton(String beanId, Class<?> clazz, Map<String, Object> properties,
            ConfigurableListableBeanFactory beanFactory) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        AbstractBeanDefinition beanDefinition = initBeanDefinition(clazz, properties);
        registry.registerBeanDefinition(beanId, beanDefinition);
    }

    /**
     * 注册BeanDefinition
     * @param beanId
     * @param clazz
     * @param properties
     * @param beanFactory
     */
    public static void registerBeanDefinition(String beanId, Class<?> clazz, Map<String, Object> properties,
            ConfigurableListableBeanFactory beanFactory) {
        if (!(beanFactory instanceof BeanDefinitionRegistry)) {
            throw new BaseException(
                    "auto get dataSource error ! ckeck beanFactory is not instanceof BeanDefinitionRegistry") {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isWriteLog() {
                    return true;
                }
            };
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        AbstractBeanDefinition beanDefinition = initBeanDefinition(clazz, properties);
        registry.registerBeanDefinition(beanId, beanDefinition);
    }

    public static AbstractBeanDefinition initBeanDefinition(Class<?> clazz, Map<String, Object> properties) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (!CollectionUtil.isEmpty(properties)) {
            properties.forEach((name, value) -> {
                builder.addPropertyValue(name, value);
            });
        }
        return builder.getBeanDefinition();
    }

}
