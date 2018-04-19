package top.wboost.common.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import top.wboost.common.annotation.parameter.ParameterConfig;
import top.wboost.common.annotation.parameter.ParameterConfigChecker;
import top.wboost.common.base.annotation.AutoWebApplicationConfig;
import top.wboost.common.exception.BusinessException;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.utils.web.interfaces.context.EzWebApplicationListener;

/**
 * 默认参数验证管理器
 * @className DefaultParameterConfigManager
 * @author jwSun
 * @date 2017年9月15日 下午2:34:03
 * @version 1.0.0
 */
@AutoWebApplicationConfig("defaultParameterConfigManager")
public class DefaultParameterConfigManager implements ParameterConfigManager, EzWebApplicationListener {

    private Logger log = LoggerUtil.getLogger(getClass());

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    Map<Class<? extends Annotation>, ParameterConfigChecker> parameterConfigMap = new HashMap<>();

    @Override
    public boolean checkParameter(Annotation parameterConfig, Object arg) {
        if (parameterConfig.annotationType().isAnnotationPresent(ParameterConfig.class)) {
            ParameterConfigChecker checker = parameterConfigMap.get(parameterConfig.annotationType());
            if (checker == null) {
                throw new BusinessException();
            }
            return checker.check(arg, parameterConfig, "");
        }
        if (log.isWarnEnabled()) {
            log.warn("Annotation {} is not a parameterConfig annotation", parameterConfig);
        }
        return false;
    }

    @Override
    public boolean checkParameter(Method domethod, Object[] args) {
        Annotation[][] paramsAnnotationsArray = domethod.getParameterAnnotations();
        for (int i = 0; i < paramsAnnotationsArray.length; i++) {
            Annotation[] paramAnnotationsArray = paramsAnnotationsArray[i];
            for (int j = 0; j < paramAnnotationsArray.length; j++) {
                Annotation paramAnnotation = paramAnnotationsArray[j];
                if (paramAnnotation.annotationType().isAnnotationPresent(ParameterConfig.class)) {
                    ParameterConfigChecker checker = parameterConfigMap.get(paramAnnotation.annotationType());
                    log.debug("get checker:{}", checker);
                    if (checker == null) {
                        throw new BusinessException();
                    }
                    if (!checker.check(args[i], paramAnnotation,
                            parameterNameDiscoverer.getParameterNames(domethod)[i])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onWebApplicationEvent(ContextRefreshedEvent event) {
        String[] checkers = event.getApplicationContext().getBeanNamesForType(ParameterConfigChecker.class);
        for (int i = 0; i < checkers.length; i++) {
            String checkerBeanName = checkers[i];
            ParameterConfigChecker checker = event.getApplicationContext().getBean(checkerBeanName,
                    ParameterConfigChecker.class);
            parameterConfigMap.put(checker.getAnnotation(), checker);
        }
        log.info("manager init:{}", parameterConfigMap);
    }

}
