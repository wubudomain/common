package top.wboost.common.base.restful;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import top.wboost.common.base.annotation.AutoWebApplicationConfig;
import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.base.service.BaseService;
import top.wboost.common.util.CopyUtil;
import top.wboost.common.util.ReflectUtil;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.interfaces.context.EzWebApplicationListener;
import top.wboost.common.utils.web.utils.SpringBeanUtil;

@AutoWebApplicationConfig
public class ResultAutoBoostHandlerMapping implements EzWebApplicationListener {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private AutoRequestMethodInvoke autoRequestMethodInvoke;

    @SuppressWarnings("unchecked")
    @Override
    public void onWebApplicationEvent(ContextRefreshedEvent event) {
        String[] baseRestfule = event.getApplicationContext().getBeanNamesForAnnotation(EnableBaseRestful.class);
        Arrays.asList(baseRestfule).forEach(name -> {
            Object controller = event.getApplicationContext().getBean(name);
            Class<?> beanType = ClassUtils.getUserClass(controller);
            if (AnnotationUtils.findAnnotation(beanType, Controller.class) == null
                    && AnnotationUtils.findAnnotation(beanType, RestController.class) == null) {
                return;
            }
            EnableBaseRestful enableBaseRestful = AnnotationUtils.findAnnotation(beanType, EnableBaseRestful.class);
            String entity = enableBaseRestful.entity();
            String inspectName = entity;
            if (!StringUtil.notEmpty(entity)) {
                int index = beanType.getSimpleName().indexOf("Controller");
                if (index != -1) {
                    inspectName = beanType.getSimpleName().substring(0, index);
                }
            }
            AutoRequestMethod requestMethod = new AutoRequestMethod();
            requestMethod.setEnableBaseRestful(enableBaseRestful);
            requestMethod.setControllerClass((Class<Object>) beanType);
            requestMethod.setControllerName(beanType.getName());
            inspectName = StringUtil.getByLowerBegin(inspectName);
            BaseService<Object, Serializable> service = getServiceByInspectName(inspectName);
            requestMethod.setService(service);
            requestMethod.setServiceClass((Class<Object>) ClassUtils.getUserClass(service));
            Class<?> entityClass = ReflectUtil.getGenericInterfaces(requestMethod.getServiceClass(), 0);
            requestMethod.setEntity(entityClass.getName());
            requestMethod.setEntityClass((Class<Object>) entityClass);
            requestMethod.setInspectName(inspectName);
            registerMethods(event, requestMethod);
        });
    }

    public void registerMethods(ContextRefreshedEvent event, AutoRequestMethod requestMethod) {
        try {
            Class<?> obj = ClassUtils.getUserClass(autoRequestMethodInvoke);
            //准备注册
            EnableBaseRestful enableBaseRestful = requestMethod.getEnableBaseRestful();
            Set<String> excludes = null;
            if (enableBaseRestful.excludes().length > 0) {
                excludes = new HashSet<>();
                for (AutoRequestMehthodType type : Arrays.asList(enableBaseRestful.excludes())) {
                    excludes.add(type.toString());
                }
            }
            Set<String> includes = null;
            if (excludes == null && enableBaseRestful.includes().length > 0) {
                includes = new HashSet<>();
                for (AutoRequestMehthodType type : Arrays.asList(enableBaseRestful.includes())) {
                    includes.add(type.toString());
                }
            }
            for (Method m : obj.getDeclaredMethods()) {
                if (m.getReturnType() == ResultEntity.class) {
                    String mName = m.getName();
                    if ((excludes != null && excludes.contains(mName))
                            || (includes != null && !includes.contains(mName))) {
                        continue;
                    }
                    AutoRequestMethod cp = CopyUtil.copyBean(AutoRequestMethod.class, requestMethod);
                    RequestMapping mapping = AnnotationUtils.getAnnotation(m, RequestMapping.class);
                    String pattern = mapping.value()[0];
                    String path = pattern.replace("{inspectName}", cp.getInspectName());
                    boolean regist = true;
                    for (Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping
                            .getHandlerMethods().entrySet()) {
                        RequestMappingInfo mappingInfo = entry.getKey();
                        if (mappingInfo.getPatternsCondition().getPatterns().contains(path) && mappingInfo
                                .getMethodsCondition().getMethods().containsAll(Arrays.asList(mapping.method()))) {
                            regist = false;
                            break;
                        }
                    }
                    if (regist) {
                        RequestMappingInfo mappingInfo = RequestMappingInfo.paths(path).methods(mapping.method())
                                .build();
                        requestMappingHandlerMapping.registerMapping(mappingInfo, "autoRequestMethodInvoke", m);
                        cp.setPath(path);
                        cp.setMethod(m);
                        autoRequestMethodInvoke.addAutoRequestMethod(path, cp);
                    }
                }
            }
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private BaseService<Object, Serializable> getServiceByInspectName(String inspectName) {
        return (BaseService<Object, Serializable>) SpringBeanUtil.getBean(inspectName + "Service");
    }

}
