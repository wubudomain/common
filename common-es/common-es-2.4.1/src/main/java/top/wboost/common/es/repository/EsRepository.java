package top.wboost.common.es.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.wboost.common.context.config.AutoProxyApplicationConfig;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoProxyApplicationConfig(proxyClass = EsRepositoryProxy.class)
public @interface EsRepository {

    /**自定义bean名,支持使用名字注入**/
    String value() default "";

}