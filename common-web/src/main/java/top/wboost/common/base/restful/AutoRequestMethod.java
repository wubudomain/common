package top.wboost.common.base.restful;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import lombok.Data;
import top.wboost.common.base.service.BaseService;

@Data
public class AutoRequestMethod {

    private String controllerName;
    private Class<Object> controllerClass;
    private String inspectName;
    private String entity;
    private Class<Object> entityClass;
    private BaseService<Object, Serializable> service;
    private Class<Object> serviceClass;
    private Method method;
    private String path;
    private EnableBaseRestful enableBaseRestful;
    private RequestMappingInfo requestMappingInfo;
    private RequestMapping requestMapping;

}
