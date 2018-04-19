package top.wboost.common.boot.config;

import java.lang.reflect.Field;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import top.wboost.common.base.ConfigForBase;
import top.wboost.common.base.annotation.AutoConfig;

/**
 * 增加spring-boot初始化
 * 更改扫描包,增加支持{@link AutoConfig}
 * @className ConfigForCommonBootApplicationContextInitializer
 * @see top.wboost.common.base.annotation.AutoConfig
 * @author jwSun
 * @date 2018年3月28日 下午4:11:50
 * @version 1.0.0
 */
@Configuration
public class ConfigForCommonBootApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if (context instanceof AnnotationConfigEmbeddedWebApplicationContext) {
            AnnotationConfigEmbeddedWebApplicationContext webContext = (AnnotationConfigEmbeddedWebApplicationContext) context;
            webContext.scan(ConfigForBase.BasePackage.allPackages());
            try {
                Field field = webContext.getClass().getDeclaredField("scanner");
                field.setAccessible(true);
                ClassPathBeanDefinitionScanner scanner = (ClassPathBeanDefinitionScanner) field.get(webContext);
                scanner.addIncludeFilter(new AnnotationTypeFilter(AutoConfig.class));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
            try {
                webContext.setBeanNameGenerator(
                        (BeanNameGenerator) Class.forName(ConfigForBase.BEAN_NAME_GENERATOR_CLASS).newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
