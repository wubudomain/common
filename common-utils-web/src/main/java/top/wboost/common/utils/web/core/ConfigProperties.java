package top.wboost.common.utils.web.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.support.StandardServletEnvironment;

import top.wboost.common.base.ConfigForBase;
import top.wboost.common.base.enums.CharsetEnum;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.interfaces.context.EzRootApplicationListener;
import top.wboost.common.utils.web.utils.PropertiesUtil;

/**
 * 配置文件 工具类
 * <pre>
 * 扫描本框架默认配置文件并注入context中
 * 优先级高于xml文件&{}扫描与@Value扫描并提供转换支持
 * 若开发者不使用&ltcontext:property-placeholder/&gt标签
 * 则将自动注册{@link PropertySourcesPlaceholderConfigurer}
 * 并启动扫描
 * 最佳支持版本：spring-4.3.13.RELEASE,spring-boot-1.5.9.RELEASE
 * </pre>
 * 
 * @see org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 * @author jwSun
 * @date 2017年3月4日 上午10:02:27
 */
public class ConfigProperties implements /* BeanDefinitionRegistryPostProcessor */BeanDefinitionRegistryPostProcessor,
        EzRootApplicationListener, EnvironmentAware, EmbeddedValueResolverAware {

    private static Logger log = LoggerUtil.getLogger(PropertiesUtil.class);

    private Environment environment;
    public static StandardServletEnvironment localenv = new StandardServletEnvironment();
    public static StringValueResolver resolver = null;
    public static final String DEFAULT_PROPERTIES = "classpath*:properties/common-default.properties";
    public static final String SYS_PROPERTIES_SCAN = "classpath*:sys/properties/*.properties";
    public static final String SYS2_PROPERTIES_SCAN = "classpath*:sys.properties/*.properties";
    public static final String DEFAULT_PROPERTIES_SCAN = "classpath:properties/*.properties";
    public static final String DEFAULT_CONFIG_FILE = "classpath:properties/config.properties";
    public static final String DEFAULT_CONFIG_NAME = "sys.properties";
    private static Set<String> ADD_PROP = new HashSet<>();
    private static PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    static {
        String location = PropertiesUtil.getProperty(DEFAULT_CONFIG_NAME, DEFAULT_CONFIG_FILE);
        if (StringUtil.notEmpty(location)) {
            ADD_PROP.add(location);
        }
        ADD_PROP.add(DEFAULT_PROPERTIES);
        ADD_PROP.add(DEFAULT_PROPERTIES_SCAN);
        ADD_PROP.add(SYS_PROPERTIES_SCAN);
        ADD_PROP.add(SYS2_PROPERTIES_SCAN);
        ADD_PROP.add(DEFAULT_CONFIG_FILE);
        ADD_PROP.forEach(path -> {
            try {
                Resource[] resources = resourceResolver.getResources(path);
                for (Resource resource : resources) {
                    localenv.getPropertySources().addLast(new ResourcePropertySource(resource));
                }
            } catch (IOException e) {
                log.warn(e.getLocalizedMessage());
            }
        });
    }

    private PropertySourcesPlaceholderConfigurer configPropertySourcesPlaceholderConfigurer(
            ConfigurableListableBeanFactory beanFactory) {
        PropertySourcesPlaceholderConfigurer configurer = null;
        try {
            String[] names = beanFactory.getBeanNamesForType(PropertySourcesPlaceholderConfigurer.class, true, false);
            if (names.length > 0) {
                configurer = beanFactory.getBean(names[0], PropertySourcesPlaceholderConfigurer.class);
            }
        } catch (Exception e) {
            // ignore
        }
        if (configurer == null) {
            configurer = new PropertySourcesPlaceholderConfigurer();
            configurer.setBeanFactory(beanFactory);
            configurer.setIgnoreUnresolvablePlaceholders(false);
            configurer.setEnvironment(environment);
            String namePrefix = "org.springframework.context.support.PropertySourcesPlaceholderConfigurer";
            String name;
            if (!initRootPropertiesConfig) {
                name = namePrefix + ConfigForBase.SCAN_CONFIG.ROOT;
            } else {
                name = namePrefix + ConfigForBase.SCAN_CONFIG.WEB;
            }
            beanFactory.registerSingleton(name, configurer);
        }
        try {
            configurer.postProcessBeanFactory(null); // 初始化propertySources
                                                     // 此方法一定会报错
        } catch (Exception e) {
            // ignore
        }
        configurer.setFileEncoding(CharsetEnum.UTF_8.getName());
        try {
            Field propertySources = configurer.getClass().getDeclaredField("propertySources");
            propertySources.setAccessible(true);
            PropertySources propertySourcesVal = (PropertySources) propertySources.get(configurer);
            Field appliedPropertySources = configurer.getClass().getDeclaredField("appliedPropertySources");
            appliedPropertySources.setAccessible(true);
            appliedPropertySources.set(configurer, propertySourcesVal);
        } catch (Exception e) {
            log.error("init PropertySourcesPlaceholderConfigurer error", e);
        }

        return configurer;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        PropertySourcesPlaceholderConfigurer configurer = configPropertySourcesPlaceholderConfigurer(beanFactory);
        mergeProperties(configurer);
    }

    private void mergeProperties(PropertySourcesPlaceholderConfigurer configurer) {
        PropertySources sources = configurer.getAppliedPropertySources();
        sources.forEach(source -> {
            Object env = source.getSource();
            if (env instanceof StandardServletEnvironment) {
                StandardServletEnvironment ssenv = (StandardServletEnvironment) env;
                ssenv.merge(localenv);
                localenv.merge(ssenv);
            }
        });
    }

    private static boolean initRootPropertiesConfig = false;

    @Override
    public void onRootApplicationEvent(ContextRefreshedEvent event) {
        initRootPropertiesConfig = true;
    }

    public boolean doWebAndRootApplicationListener(ContextRefreshedEvent event) {
        return false;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        ConfigProperties.resolver = resolver;
    }

}
