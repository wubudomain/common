package top.wboost.common.manager;

import top.wboost.common.annotation.parameter.ParameterConfig;

public interface AnnotationManager {

    public Object resolve(Class<? extends ParameterConfig> annotationClass);

}
