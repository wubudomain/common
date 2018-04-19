package top.wboost.boot.configuration.datasource.mybatis.spring.boot.starter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.Data;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;
import top.wboost.base.spring.boot.starter.CustomerPropertiesTreeUtil;
import top.wboost.boot.configuration.datasource.spring.boot.autoconfigure.GlobalForDataSourceBootStarter;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.utils.ConvertUtil;

@Configuration
@EnableConfigurationProperties(MybatisMultipleProperties.class)
public class MybatisAutoMultipleConfigurationBean {

    private static final Logger logger = LoggerFactory.getLogger(MybatisAutoMultipleConfigurationBean.class);

    @Autowired
    private DefaultListableBeanFactory beanFactory;
    @Autowired
    private List<DataSource> dataSources;

    /**dsName:MultipleConfiguration**/
    private Map<String, MultipleConfiguration> mybatisAutoConfiguration = new HashMap<>();
    private final MybatisMultipleProperties properties;

    private final ResourceLoader resourceLoader;
    private Map<String, SqlSessionFactory> sqlSessionFactorys = new HashMap<>();
    private Map<String, SqlSessionTemplate> sqlSessionTemplates = new HashMap<>();

    public MybatisAutoMultipleConfigurationBean(MybatisMultipleProperties properties,
            ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader,
            ObjectProvider<DatabaseIdProvider> databaseIdProvider,
            ObjectProvider<List<tk.mybatis.mapper.autoconfigure.ConfigurationCustomizer>> configurationCustomizersProvider) {
        this.properties = properties;
        this.resourceLoader = resourceLoader;
        Map<String, MybatisMultipleProperties> MybatisMultipleProperties = CustomerPropertiesTreeUtil
                .resolvePropertiesTree(MybatisMultipleProperties.class, this.properties, "common.mybatis",
                        "sqlSessionTemplate", "dataSource");
        MybatisMultipleProperties.forEach((sqlSessionTemplateName, mybatisProperties) -> {
            //若没有配置连接池则使用默认主连接池
            if (!StringUtil.notEmpty(mybatisProperties.getDataSource())) {
                logger.warn("mybatis SqlSessionTemplate ： {} ,未配置使用的数据库连接池,将使用默认主连接池：{} ,建议指定!", sqlSessionTemplateName,
                        GlobalForDataSourceBootStarter.PRIMARY_DATASOURCE_NAME);
                mybatisProperties.setDataSource(GlobalForDataSourceBootStarter.PRIMARY_DATASOURCE_NAME);
            }
            MybatisProperties cpProperties = ConvertUtil
                    .mapConvertToBean(ConvertUtil.beanConvertToMap(mybatisProperties), MybatisProperties.class);
            MultipleConfiguration config = new MultipleConfiguration(
                    sqlSessionTemplateName, new MapperAutoConfiguration(cpProperties, interceptorsProvider,
                            resourceLoader, databaseIdProvider, configurationCustomizersProvider),
                    mybatisProperties.getDataSource(), mybatisProperties);
            mybatisAutoConfiguration.put(mybatisProperties.getDataSource(), config);
        });
    }

    @PostConstruct
    public void checkConfigFileExists() {
        if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
            Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource
                    + " (please add config file or check your Mybatis configuration)");
        }

    }

    @Data
    class MultipleConfiguration {
        String sqlSessionTemplateName;
        MybatisMultipleProperties properties;
        /**默认使用{dataSourceName}_sqlSessionFactory_for_{sqlSessionTemplateName}**/
        String sqlSessionFactoryName;
        MapperAutoConfiguration mybatisAutoConfiguration;
        String dataSourceName;

        public MultipleConfiguration(String sqlSessionTemplateName, MapperAutoConfiguration mybatisAutoConfiguration,
                String dataSourceName, MybatisMultipleProperties properties) {
            super();
            if ("primary".equals(sqlSessionTemplateName)) {
                this.sqlSessionTemplateName = "sqlSessionTemplate";
            } else {
                this.sqlSessionTemplateName = sqlSessionTemplateName;
            }
            this.mybatisAutoConfiguration = mybatisAutoConfiguration;
            this.dataSourceName = dataSourceName;
            this.properties = properties;
            this.sqlSessionFactoryName = StringUtil.notEmpty(properties.getSqlSessionFactoryName())
                    ? properties.getSqlSessionFactoryName() : "sqlSessionFactory";
        }

    }

    @PostConstruct
    public void initConfig() {
        if (this.mybatisAutoConfiguration.size() == 0) {
            return;
        }
        String[] dataSources = this.beanFactory.getBeanNamesForType(DataSource.class);
        Set<String> dsNames = new HashSet<String>(Arrays.asList(dataSources));
        Set<String> dsAndAliasNames = new HashSet<String>(Arrays.asList(dataSources));
        dsAndAliasNames.addAll(dsNames);
        dsNames.forEach(dsName -> {
            String[] alias = this.beanFactory.getAliases(dsName);
            if (alias.length > 0) {
                dsAndAliasNames.addAll(Arrays.asList(alias));
            }
        });
        dsAndAliasNames.forEach(dsName -> {
            // 若此连接池未在mybatis中配置则跳过
            if (!this.mybatisAutoConfiguration.containsKey(dsName)) {
                return;
            }
            DataSource dataSource = this.beanFactory.getBean(dsName, DataSource.class);
            MultipleConfiguration config = this.mybatisAutoConfiguration.get(dsName);
            try {
                SqlSessionFactory sqlSessionFactory = config.getMybatisAutoConfiguration()
                        .sqlSessionFactory(dataSource);
                SqlSessionTemplate sqlSessionTemplate = config.getMybatisAutoConfiguration()
                        .sqlSessionTemplate(sqlSessionFactory);
                if (logger.isInfoEnabled()) {
                    logger.info("registerSingleton sqlSessionFactory bean: {}", config.getSqlSessionFactoryName());
                }
                this.beanFactory.registerSingleton(config.getSqlSessionFactoryName(), sqlSessionFactory);

                if (logger.isInfoEnabled()) {
                    logger.info("registerSingleton sqlSessionTemplateName bean: {}",
                            config.getSqlSessionTemplateName());
                }
                this.beanFactory.registerSingleton(config.getSqlSessionTemplateName(), sqlSessionTemplate);
                this.sqlSessionFactorys.put(config.getSqlSessionFactoryName(), sqlSessionFactory);
                this.sqlSessionTemplates.put(config.getSqlSessionTemplateName(), sqlSessionTemplate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SqlSessionFactory getDefaultSqlSessionFactory() {
        if (this.sqlSessionFactorys.size() == 0) {
            return null;
        }
        return this.sqlSessionFactorys.get("sqlSessionFactory");
    }

    public SqlSessionTemplate getDefaultSqlSessionTemplate() {
        if (this.sqlSessionTemplates.size() == 0) {
            return null;
        }
        return this.sqlSessionTemplates.get("sqlSessionTemplate");
    }

}