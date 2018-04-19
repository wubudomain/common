package top.wboost.common.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可分主次注入
 * @className AutoConfig
 * @author jwSun
 * @date 2017年9月13日 下午8:17:08
 * @version 1.0.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceConfig {

    /**主注入，若有此bean则注入**/
    String primary();

    /**次要注入，若无主bean则注入**/
    String secondary();

}
