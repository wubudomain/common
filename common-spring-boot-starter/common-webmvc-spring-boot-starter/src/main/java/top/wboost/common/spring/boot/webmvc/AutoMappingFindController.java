package top.wboost.common.spring.boot.webmvc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import lombok.Data;
import top.wboost.common.annotation.Explain;
import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.system.code.SystemCode;
import top.wboost.common.util.ReflectUtil;

@RestController
@RequestMapping("/webmvc/mapping")
public class AutoMappingFindController implements InitializingBean {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private MultiValueMap<String, RequestMappingInfo> urlLookup;

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

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

    @Data
    class ReturnInfo {
        RequestMappingInfo requestMappingInfo;
        Map<String, Object> requestMappingInfoSimple = new HashMap<>();
        HandlerMethod handlerMethod;
        /**parameterName:java.lang.String**/
        Map<String, String> parameterTypes = new LinkedHashMap<>();
        String returnType;
        String methodName;

        public ReturnInfo(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
            super();
            this.requestMappingInfo = requestMappingInfo;
            if (!requestMappingInfo.getConsumesCondition().isEmpty()) {
                requestMappingInfoSimple.put("consumesCondition", requestMappingInfo.getConsumesCondition());
            }
            if (!requestMappingInfo.getHeadersCondition().isEmpty()) {
                requestMappingInfoSimple.put("HeadersCondition", requestMappingInfo.getHeadersCondition());
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
            List<MethodParameter> methodParameterList = Arrays.asList(handlerMethod.getMethodParameters());
            String[] names = parameterNameDiscoverer.getParameterNames(handlerMethod.getMethod());
            for (int i = 0; i < methodParameterList.size(); i++) {
                MethodParameter methodParameter = methodParameterList.get(i);
                String name = names[i];
                String type = methodParameter.getParameterType().getName();
                parameterTypes.put(name, type);
            }
        }
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
            Object mappingRegistry = field.get(requestMappingHandlerMapping);
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

}
