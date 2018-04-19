package top.wboost.boot.configuration.datasource.spring.boot.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启JPA功能
 * @className EnableJpa
 * @author jwSun
 * @date 2018年4月16日 下午9:49:07
 * @version 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableJpa {

}
