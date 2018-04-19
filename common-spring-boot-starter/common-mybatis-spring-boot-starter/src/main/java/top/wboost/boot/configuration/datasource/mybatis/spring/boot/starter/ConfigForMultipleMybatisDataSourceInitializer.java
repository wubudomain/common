package top.wboost.boot.configuration.datasource.mybatis.spring.boot.starter;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

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
public class ConfigForMultipleMybatisDataSourceInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        /*if (context instanceof AnnotationConfigEmbeddedWebApplicationContext) {
            AnnotationConfigEmbeddedWebApplicationContext webContext = (AnnotationConfigEmbeddedWebApplicationContext) context;
            try {
                Field field = webContext.getClass().getDeclaredField("scanner");
                field.setAccessible(true);
                ClassPathBeanDefinitionScanner scanner = (ClassPathBeanDefinitionScanner) field.get(webContext);
                scanner.addExcludeFilter(new AssignableTypeFilter(MybatisAutoConfiguration.class));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }*/
    }

}
