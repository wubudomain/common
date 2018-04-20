
package top.wboost.boot.configuration.datasource.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import top.wboost.base.spring.boot.starter.GlobalForSpringBootStarter;

/**
 * 此类为IDE提示方便,无用
 * @className DataSourceWrapper
 * @author jwSun
 * @date 2018年4月14日 下午11:58:10
 * @version 1.0.0
 */
@Data
@ConfigurationProperties(GlobalForSpringBootStarter.PROPERTIES_PREFIX + "datasource.primary")
public class DataSourceWrapperShow {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String validationQuery;
    //private Integer minPoolSize = 1;
    //private Integer maxPoolSize = 3;
    private Integer maxActive = 500;
    private Integer minIdle = 3;
    //private Integer maxIdle = 500;
    private Integer initialSize = 3;
    private Integer maxWait = 10000;
    private Boolean removeAbandoned = true;
    private Integer removeAbandonedTimeout = 20;
    /**每30秒运行一次空闲连接回收器 **/
    private Integer timeBetweenEvictionRunsMillis = 3000000;
    /**池中的连接空闲30分钟后被回收,默认值就是30分钟。**/
    private Integer minEvictableIdleTimeMillis = 1800000;
    /**表示每timeBetweenEvictionRunsMillis秒，取出3条连接，使用validationQuery中的SQL进行测试 ，测试不成功就销毁连接。销毁连接后，连接数量就少了，如果小于minIdle数量，就新建连接。**/
    private Boolean testWhileIdle = true;
    private Boolean testOnBorrow = false;
    private Boolean testOnReturn = false;
    private Boolean poolPreparedStatements = true;
    private Integer maxPoolPreparedStatementPerConnectionSize = 20;

}
