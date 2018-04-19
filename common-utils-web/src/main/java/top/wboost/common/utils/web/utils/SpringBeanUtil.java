package top.wboost.common.utils.web.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import top.wboost.common.base.annotation.AutoRootApplicationConfig;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;

/**
 * 获得root spring bean对象 工具类
 * @author jwSun
 * @date 2017年2月6日 下午5:49:59
 */
@AutoRootApplicationConfig("springBeanUtil")
public class SpringBeanUtil implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static Logger log = LoggerUtil.getLogger(SpringBeanUtil.class);

    private static ApplicationContext applicationContext;

    public static Object getBean(String beanId) {
        try {
            return applicationContext.getBean(beanId);
        } catch (Exception e) {
            log.warn("spring bean :{} cant find.", beanId);
            return null;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            log.warn("spring bean :{} cant find.", clazz.getName());
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

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 使用BeanFactoryPostProcessor接口,在PostProcessorRegistrationDelegate初始化时,ApplicationContextAware 执行顺序将提前
    }

}
