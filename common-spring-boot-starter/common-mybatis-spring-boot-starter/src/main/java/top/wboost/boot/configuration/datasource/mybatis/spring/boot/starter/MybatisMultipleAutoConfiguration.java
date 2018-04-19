package top.wboost.boot.configuration.datasource.mybatis.spring.boot.starter;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionTemplate.class, SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MybatisMultipleProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({ tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration.class,
        org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration.class })
@Import(MybatisAutoMultipleConfigurationBean.class)
public class MybatisMultipleAutoConfiguration {

    @Autowired
    MybatisAutoMultipleConfigurationBean mybatisAutoMultipleConfigurationBean;
    @Autowired
    DataSource dataSource;

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() {
        return mybatisAutoMultipleConfigurationBean.getDefaultSqlSessionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate() {
        return mybatisAutoMultipleConfigurationBean.getDefaultSqlSessionTemplate();
    }

}
