package top.wboost.boot.configuration.datasource.spring.boot.autoconfigure;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

import top.wboost.common.log.util.LoggerUtil;

@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(DataSourceWrapper.class)
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@ConditionalOnClass(DataSource.class)
@Import(DataSourceAutoMultipleConfigurationBean.class)
public class DataSourceMultipleAutoConfiguration {

    @Autowired
    DataSourceAutoMultipleConfigurationBean dataSourceAutoMultipleConfigurationBean;

    Logger log = LoggerUtil.getLogger(DataSourceMultipleAutoConfiguration.class);

    @Bean(initMethod = "init")
    public DataSource dataSource() {
        return dataSourceAutoMultipleConfigurationBean.initConfig();
    }

}