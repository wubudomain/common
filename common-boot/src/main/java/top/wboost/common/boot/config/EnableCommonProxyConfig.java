package top.wboost.common.boot.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ImportResource;

/**
 * 开启代理配置功能
 * @className EnableCommonProxyConfig
 * @author jwSun
 * @date 2018年4月14日 下午2:01:48
 * @version 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportResource(locations = { "classpath*:sys/spring/wboost-spring-context-init.xml" })
public @interface EnableCommonProxyConfig {

}
