package top.wboost.common.context.generator;

import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import top.wboost.common.base.annotation.AutoConfig;
import top.wboost.common.base.annotation.AutoRootApplicationConfig;
import top.wboost.common.base.annotation.AutoWebApplicationConfig;
import top.wboost.common.context.config.AutoProxyApplicationConfig;

@AutoConfig
public class ConfigAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {

    private static final String COMPONENT_ANNOTATION_CLASSNAME_AUTO = AutoConfig.class.getName();
    private static final String COMPONENT_ANNOTATION_CLASSNAME_ROOT = AutoRootApplicationConfig.class.getName();
    private static final String COMPONENT_ANNOTATION_CLASSNAME_WEB = AutoWebApplicationConfig.class.getName();
    private static final String COMPONENT_ANNOTATION_CLASSNAME_PROXY = AutoProxyApplicationConfig.class.getName();

    protected boolean isStereotypeWithNameValue(String annotationType, Set<String> metaAnnotationTypes,
            Map<String, Object> attributes) {
        boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME_ROOT)
                || annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME_WEB)
                || annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME_PROXY)
                || annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME_AUTO)
                || (metaAnnotationTypes != null && metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME_AUTO));

        boolean result = (isStereotype && attributes != null && attributes.containsKey("value"));
        if (!result) {
            return super.isStereotypeWithNameValue(annotationType, metaAnnotationTypes, attributes);
        } else {
            return result;
        }
    }

}
