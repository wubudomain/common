package top.wboost.common.spring.boot.webmvc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import springfox.documentation.spring.web.PropertySourcedRequestMappingHandlerMapping;
import springfox.documentation.spring.web.json.Json;
import top.wboost.common.annotation.Explain;
import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.spring.boot.webmvc.annotation.ApiVersion;
import top.wboost.common.system.code.SystemCode;
import top.wboost.common.util.ReflectUtil;
import top.wboost.common.utils.web.interfaces.context.EzWebApplicationListener;
import top.wboost.common.utils.web.utils.SpringBeanUtil;

@RestController
@RequestMapping("/webmvc/mapping")
public class AutoMappingFindController implements InitializingBean, EzWebApplicationListener {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private MultiValueMap<String, RequestMappingInfo> urlLookup;

    private static ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    HandlerMethod method = null;
    Object handler = null;

    @GetMapping
    @Explain(value = "查询所有接口")
    public ResultEntity getAllMapping() {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        List<ReturnInfo> result = new ArrayList<>();
        map.forEach((mappingInfo, handlerMethod) -> {
            result.add(new ReturnInfo(mappingInfo, handlerMethod));
        });
        return ResultEntity.success(SystemCode.QUERY_OK).setData(result)
                .setFilterNames("handlerMethod", "requestMappingInfo").build();
    }

    @SuppressWarnings("unchecked")
    @GetMapping("docs")
    @Explain(value = "查询所有接口")
    public ResultEntity getAllMappingBySwagger(@RequestParam(value = "group", required = false) String swaggerGroup,
            HttpServletRequest servletRequest) {
        //Swagger swagger = null;
        ResponseEntity<Json> response = null;
        try {
            response = (ResponseEntity<Json>) method.getMethod().invoke(handler, swaggerGroup, servletRequest);
            /*String body = response.getBody().toString();
            swagger = JSONObject.parseObject(response.getBody().toString(), Swagger.class);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject json = JSONObject.parseObject(response.getBody().value());
        json.getJSONObject("paths").forEach((path, jo) -> {
            List<HandlerMethod> list = requestMappingHandlerMapping.getHandlerMethodsForMappingName(path);
            System.out.println(list);
        });
        return ResultEntity.success(SystemCode.QUERY_OK).setData(response.getBody()).build();
    }

    @Data
    public static class ReturnInfo {
        RequestMappingInfo requestMappingInfo;
        Map<String, Object> requestMappingInfoSimple = new HashMap<>();
        HandlerMethod handlerMethod;
        /**parameterName:java.lang.String**/
        List<ParameterInfo> parameterTypes = new ArrayList<>();
        String returnType;
        String methodName;
        String version;

        public ReturnInfo(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
            super();
            this.requestMappingInfo = requestMappingInfo;
            if (!requestMappingInfo.getConsumesCondition().isEmpty()) {
                requestMappingInfoSimple.put("consumesCondition", requestMappingInfo.getConsumesCondition());
            }
            if (!requestMappingInfo.getHeadersCondition().isEmpty()) {
                requestMappingInfoSimple.put("headersCondition", requestMappingInfo.getHeadersCondition());
            }
            if (!requestMappingInfo.getMethodsCondition().isEmpty()) {
                requestMappingInfoSimple.put("methodsCondition", requestMappingInfo.getMethodsCondition());
            }
            if (!requestMappingInfo.getParamsCondition().isEmpty()) {
                requestMappingInfoSimple.put("paramsCondition", requestMappingInfo.getParamsCondition());
            }
            if (!requestMappingInfo.getPatternsCondition().isEmpty()) {
                requestMappingInfoSimple.put("patternsCondition", requestMappingInfo.getPatternsCondition());
            }
            if (!requestMappingInfo.getProducesCondition().isEmpty()) {
                requestMappingInfoSimple.put("producesCondition", requestMappingInfo.getProducesCondition());
            }
            this.handlerMethod = handlerMethod;
            this.methodName = handlerMethod.getMethod().getName();
            this.returnType = handlerMethod.getReturnType().getParameterType().getName();
            ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiVersion.class);
            if (apiVersion == null) {
                this.version = "1.0.0";
            } else {
                this.version = apiVersion.value();
            }
            List<MethodParameter> methodParameterList = Arrays.asList(handlerMethod.getMethodParameters());
            String[] names = parameterNameDiscoverer.getParameterNames(handlerMethod.getMethod());
            for (int i = 0; i < methodParameterList.size(); i++) {
                ParameterInfo info = new ParameterInfo();
                MethodParameter methodParameter = methodParameterList.get(i);
                String name = names[i];
                String type = methodParameter.getParameterType().getName();
                info.setIndex(i);
                info.setName(name);
                info.setRemark(null);
                ApiParam apiParam = methodParameter.getParameterAnnotation(ApiParam.class);
                if (apiParam == null) {
                    info.setRequire(false);
                    info.setRemark("无");
                } else {
                    info.setRequire(apiParam.required());
                    info.setRemark(apiParam.value());
                }
                info.setJavaType(type);
                parameterTypes.add(info);
            }
        }

        public ReturnInfo() {
            super();
        }
    }

    /*@Data
    public static class RequestMappingInfoSimple {
        private ConsumesRequestCondition  consumesRequestCondition ;
        private HeadersRequestCondition  headersRequestCondition ;
        private RequestMethodsRequestCondition  methodsCondition ;
        private ParamsRequestCondition paramsCondition;
        private PatternsRequestCondition  patternsCondition;
        private ProducesRequestCondition producesCondition;
    }*/

    @Data
    public static class ParameterInfo {
        private String name;
        private Integer index;
        private String javaType;
        private String remark;
        private boolean require;
    }

    @GetMapping("/url")
    @Explain(value = "查询对应路径接口")
    public ResultEntity getByUrl(String url) {
        List<RequestMappingInfo> list;
        if (urlLookup != null) {
            list = urlLookup.get(url);
        } else {
            list = new ArrayList<>();
            for (Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping.getHandlerMethods()
                    .entrySet()) {
                RequestMappingInfo mappingInfo = entry.getKey();
                if (mappingInfo.getPatternsCondition().getPatterns().contains(url)) {
                    list.add(mappingInfo);
                }
            }
        }
        List<ReturnInfo> result = new ArrayList<>();
        list.forEach(mappingInfo -> {
            HandlerMethod method = requestMappingHandlerMapping.getHandlerMethods().get(mappingInfo);
            result.add(new ReturnInfo(mappingInfo, method));
        });
        return ResultEntity.success(SystemCode.QUERY_OK).setData(result)
                .setFilterNames("handlerMethod", "requestMappingInfo").build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Field field = ReflectUtil.findField(requestMappingHandlerMapping.getClass(), "mappingRegistry");
        if (field != null) {
            field.setAccessible(true);
            mappingRegistry = field.get(requestMappingHandlerMapping);
            if (mappingRegistry != null) {
                Field urlLookupField = ReflectUtil.findField(mappingRegistry.getClass(), "urlLookup");
                urlLookupField.setAccessible(true);
                if (urlLookupField != null) {
                    @SuppressWarnings("unchecked")
                    MultiValueMap<String, RequestMappingInfo> urlLookup = (MultiValueMap<String, RequestMappingInfo>) urlLookupField
                            .get(mappingRegistry);
                    this.urlLookup = urlLookup;
                }
            }
        }
    }

    Object mappingRegistry = null;

    @Override
    public void onWebApplicationEvent(ContextRefreshedEvent event) {
        try {
            PropertySourcedRequestMappingHandlerMapping p = SpringBeanUtil
                    .getBean(PropertySourcedRequestMappingHandlerMapping.class);
            Field handlerMethodsField = ReflectUtil.findField(PropertySourcedRequestMappingHandlerMapping.class,
                    "handlerMethods");
            handlerMethodsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, HandlerMethod> handlerMethods = (Map<String, HandlerMethod>) handlerMethodsField.get(p);
            this.method = handlerMethods.get("/v2/api-docs");
            Field handlerField = ReflectUtil.findField(PropertySourcedRequestMappingHandlerMapping.class, "handler");
            handlerField.setAccessible(true);
            this.handler = handlerField.get(p);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
