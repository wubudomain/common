package top.wboost.common.kylin.support.repository.v2.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.wboost.common.context.config.AutoProxyApplicationConfig;
import top.wboost.common.kylin.support.repository.v2.core.KylinRepositoryProxy;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoProxyApplicationConfig(proxyClass = KylinRepositoryProxy.class)
public @interface KylinRepository {

    /**自定义bean名,支持使用名字注入**/
    String value() default "";

    /**kylin project名称 与  cubeName 必须选择一个填写 如知道project名推荐使用此属性**/
    String project() default "";

    /**如不知道project名则填写此属性**/
    String cubeName() default "";

}