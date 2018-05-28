package top.wboost.common.spring.boot.webmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法版本号
 * @className ApiVersion
 * @author jwSun
 * @date 2018年5月28日 下午2:40:57
 * @version 1.0.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    String value() default "1.0.0";

}