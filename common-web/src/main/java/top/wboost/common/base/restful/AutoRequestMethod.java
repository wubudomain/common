package top.wboost.common.base.restful;

import java.io.Serializable;
import java.lang.reflect.Method;

import top.wboost.common.base.service.BaseJpaService;

public class AutoRequestMethod {

    private String controllerName;
    private Class<Object> controllerClass;
    private String inspectName;
    private String entity;
    private Class<Object> entityClass;
    private BaseJpaService<Object, Serializable> service;
    private Class<Object> serviceClass;
    private Method method;
    private String path;
    private EnableBaseRestful enableBaseRestful;

    public EnableBaseRestful getEnableBaseRestful() {
        return enableBaseRestful;
    }

    public void setEnableBaseRestful(EnableBaseRestful enableBaseRestful) {
        this.enableBaseRestful = enableBaseRestful;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public Class<Object> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<Object> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getInspectName() {
        return inspectName;
    }

    public void setInspectName(String inspectName) {
        this.inspectName = inspectName;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Class<Object> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<Object> entityClass) {
        this.entityClass = entityClass;
    }

    public BaseJpaService<Object, Serializable> getService() {
        return service;
    }

    public void setService(BaseJpaService<Object, Serializable> service) {
        this.service = service;
    }

    public Class<Object> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<Object> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
