package top.wboost.common.annotation.parameter;

import java.lang.annotation.Annotation;

import top.wboost.common.base.annotation.AutoWebApplicationConfig;
import top.wboost.common.util.Checker;
import top.wboost.common.util.StringUtil;

/**
 * NotNull参数
 * @className NotNullChecker
 * @author jwSun
 * @date 2017年9月15日 下午2:44:42
 * @version 1.0.0
 */
@AutoWebApplicationConfig
public class NotNullChecker implements ParameterConfigChecker {

    @Override
    public Boolean check(Object source, Annotation annotation, Object... args) {
        NotNull notnull = (NotNull) annotation;
        if (StringUtil.notEmpty(notnull.value())) {
            check(source, notnull.value());
        } else {
            check(source, args);
        }
        return true;
    }

    @Override
    public Boolean check(Object source, Object... args) {
        Checker.notNull(source, (String) args[0]);
        return true;
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return NotNull.class;
    }

}
