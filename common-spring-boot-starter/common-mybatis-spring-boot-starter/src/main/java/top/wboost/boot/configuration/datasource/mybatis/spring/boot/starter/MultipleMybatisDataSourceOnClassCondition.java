package top.wboost.boot.configuration.datasource.mybatis.spring.boot.starter;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class MultipleMybatisDataSourceOnClassCondition implements AutoConfigurationImportFilter {

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] match = new boolean[autoConfigurationClasses.length];
        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            /*if ("org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration".equals(autoConfigurationClasses[i])
                    || "tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration".equals(autoConfigurationClasses[i])) {
                match[i] = false;
            } else {*/
            match[i] = true;
            /*}*/
        }
        return match;
    }

}