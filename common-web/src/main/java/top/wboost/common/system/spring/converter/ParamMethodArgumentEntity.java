package top.wboost.common.system.spring.converter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为springmvc可自动转换类,搭配自定义converter使用
 * @className ParamMethodArgumentEntity
 * @author jwSun
 * @date 2017年12月15日 下午5:25:17
 * @version 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamMethodArgumentEntity {

}