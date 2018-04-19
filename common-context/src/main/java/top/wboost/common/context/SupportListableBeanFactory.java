package top.wboost.common.context;

import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import top.wboost.common.context.annotation.ResourceConfig;
import top.wboost.common.util.StringUtil;

public class SupportListableBeanFactory extends DefaultListableBeanFactory {

    public SupportListableBeanFactory(BeanFactory internalParentBeanFactory) {
        super(internalParentBeanFactory);
    }
    
    public SupportListableBeanFactory() {
        super();
    }

    protected String determineAutowireCandidate(Map<String, Object> candidates, DependencyDescriptor descriptor) {
        String autowiredBeanName = null;
        autowiredBeanName = super.determineAutowireCandidate(candidates, descriptor);
        if (!StringUtil.notEmpty(autowiredBeanName)) {
            ResourceConfig config = descriptor.getAnnotatedElement().getAnnotation(ResourceConfig.class);
            if (config != null) {
                boolean hasPrimary = candidates.containsKey(config.primary());
                if (hasPrimary) {
                    autowiredBeanName = config.primary();
                } else if (candidates.containsKey(config.secondary())) {
                    autowiredBeanName = config.secondary();
                }
                if (StringUtil.notEmpty(autowiredBeanName)) {
                    logger.info("has two bean instance for " + descriptor.getDependencyType() + " , use bean name : "
                            + autowiredBeanName);
                }
            }
        }
        return autowiredBeanName;
    }

}
