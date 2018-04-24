package top.wboost.boot.configuration.datasource.spring.boot.autoconfigure;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * 配置使用JPA或MYBATIS
 * @className OrmChooseAutoConfigurationImportFilter
 * @author jwSun
 * @date 2018年4月16日 下午9:47:22
 * @version 1.0.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrmChooseAutoConfigurationImportFilter implements AutoConfigurationImportFilter {

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        Set<String> excludes = new HashSet<>();
        try {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement element = stackTraceElements[stackTraceElements.length - 1];
            String bootRunName = element.getClassName();
            Class<?> clazz = Class.forName(bootRunName);
            EnableJpa enableJpa = AnnotationUtils.findAnnotation(clazz, EnableJpa.class);
            if (enableJpa == null) {
                excludes.add(HibernateJpaAutoConfiguration.class.getName());
                excludes.add(JpaRepositoriesAutoConfiguration.class.getName());
            }
            excludes.add(DruidDataSourceAutoConfigure.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        boolean[] match = new boolean[autoConfigurationClasses.length];
        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            if (excludes.contains(autoConfigurationClasses[i])) {
                match[i] = false;
            } else {
                match[i] = true;
            }
        }
        return match;
    }

}