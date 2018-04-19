package top.wboost.common.annotation.parameter;

import java.lang.annotation.Annotation;

import top.wboost.common.base.interfaces.Checker;

/**
 * 参数验证接口
 * @className ParameterConfigChecker
 * @author jwSun
 * @date 2017年9月15日 下午2:44:28
 * @version 1.0.0
 */
public interface ParameterConfigChecker extends Checker<Object, Boolean> {

    /**
     * 验证
     * @param source 参数值
     * @param annotation 注解
     * @param args 扩展参数
     * @return boolean
     */
    public Boolean check(Object source, Annotation annotation, Object... args);

    public Class<? extends Annotation> getAnnotation();

    //public Annotation getSupportsAnnotations();

}
