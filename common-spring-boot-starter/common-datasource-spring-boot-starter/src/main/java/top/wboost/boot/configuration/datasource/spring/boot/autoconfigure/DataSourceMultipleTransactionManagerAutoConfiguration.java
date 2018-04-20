package top.wboost.boot.configuration.datasource.spring.boot.autoconfigure;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;

/**
 * 多数据源TransactionManager自动初始化
 * @className DataSourceTransactionManagerAutoConfiguration
 * @author jwSun
 * @date 2018年4月20日 下午3:15:04
 * @version 1.0.0
 */
@Configuration
@ConditionalOnClass({ JdbcTemplate.class, PlatformTransactionManager.class })
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@AutoConfigureBefore(org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration.class)
@AutoConfigureAfter(DataSourceMultipleAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceMultipleTransactionManagerAutoConfiguration {

    @Configuration
    static class DataSourceTransactionManagerConfiguration {

        private Logger log = LoggerUtil.getLogger(DataSourceTransactionManagerConfiguration.class);

        @Autowired
        private DefaultListableBeanFactory beanFactory;

        private final TransactionManagerCustomizers transactionManagerCustomizers;

        DataSourceTransactionManagerConfiguration(
                ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
            this.transactionManagerCustomizers = transactionManagerCustomizers.getIfAvailable();
        }

        @Bean
        @Primary
        @ConditionalOnMissingBean(PlatformTransactionManager.class)
        public DataSourceTransactionManager transactionManager() {
            String[] dataSources = this.beanFactory.getBeanNamesForType(DataSource.class, true, false);
            DataSourceTransactionManager primaryTransactionManager = null;
            if (dataSources.length == 1) {
                primaryTransactionManager = new DataSourceTransactionManager(
                        this.beanFactory.getBean(dataSources[0], DataSource.class));
            } else {
                Set<String> set = new HashSet<>(Arrays.asList(dataSources));
                if (set.contains(GlobalForDataSourceBootStarter.PRIMARY_DATASOURCE_NAME)) {
                    primaryTransactionManager = new DataSourceTransactionManager(this.beanFactory
                            .getBean(GlobalForDataSourceBootStarter.PRIMARY_DATASOURCE_NAME, DataSource.class));
                    log.info("regist primary transactionManager: {} for {}", "transactionManager",
                            GlobalForDataSourceBootStarter.PRIMARY_DATASOURCE_NAME);
                    set.remove(GlobalForDataSourceBootStarter.PRIMARY_DATASOURCE_NAME);
                }
                for (String dsName : set) {
                    if (primaryTransactionManager == null) {
                        primaryTransactionManager = new DataSourceTransactionManager(
                                this.beanFactory.getBean(dsName, DataSource.class));
                        log.info("regist primary transactionManager: {} for {}", "transactionManager", dsName);
                    } else {
                        this.beanFactory.registerSingleton(dsName + "_TransactionManager",
                                new DataSourceTransactionManager(this.beanFactory.getBean(dsName, DataSource.class)));
                        log.info("regist transactionManager: {} for {}", dsName + "_TransactionManager", dsName);
                    }
                }
            }
            if (this.transactionManagerCustomizers != null) {
                this.transactionManagerCustomizers.customize(primaryTransactionManager);
            }
            return primaryTransactionManager;
        }

    }

}