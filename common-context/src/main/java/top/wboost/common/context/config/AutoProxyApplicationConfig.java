package top.wboost.common.context.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * proxy application自动扫描注解
 * @className AutoWebApplicationConfig
 * @author jwSun
 * @date 2017年9月13日 下午8:16:35
 * @version 1.0.0
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoProxyApplicationConfig {

    /**
     * 创建bean名
     * @return value
     */
    String value() default "";

    /**
     * 代理类执行
     * @return proxyClass
     */
    Class<? extends AutoProxy> proxyClass();

}
