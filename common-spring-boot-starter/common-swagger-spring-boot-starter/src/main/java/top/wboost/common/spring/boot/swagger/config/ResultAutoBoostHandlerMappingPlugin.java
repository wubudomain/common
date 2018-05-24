package top.wboost.common.spring.boot.swagger.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;

import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;
import top.wboost.common.annotation.Explain;
import top.wboost.common.base.restful.AutoRequestMehthodType;
import top.wboost.common.base.restful.AutoRequestMethod;
import top.wboost.common.base.restful.AutoRequestMethodInvoke;
import top.wboost.common.spring.boot.swagger.template.QueryPageTemplate;

/**
 * 增加 @EnableBaseRestful 注解新增的各种接口
 * @className ResultAutoBoostHandlerMappingPlugin
 * @author jwSun
 * @date 2018年5月24日 下午5:12:40
 * @version 1.0.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 10)
public class ResultAutoBoostHandlerMappingPlugin implements ApiListingScannerPlugin {

    private Map<String, Map<String, AutoRequestMethod>> map;
    private CachingOperationNameGenerator nameGenerator = new CachingOperationNameGenerator();
    private final ModelAttributeParameterExpander expander;

    public ResultAutoBoostHandlerMappingPlugin(ModelAttributeParameterExpander expander,
            AutoRequestMethodInvoke autoRequestMethodInvoke) throws IllegalArgumentException, IllegalAccessException {
        super();
        this.expander = expander;
        Map<String, Map<String, AutoRequestMethod>> map = autoRequestMethodInvoke.getAutoRequestMethod();
        if (map != null && map.size() > 0) {
            this.map = map;
        }
    }

    public List<Parameter> generatorParameters(AutoRequestMethod autoRequestMethod,
            DocumentationContext documentationContext) {
        List<Parameter> parameters = new ArrayList<>();
        AutoRequestMehthodType type = AutoRequestMehthodType.valueOf(autoRequestMethod.getMethod().getName());
        if (type == AutoRequestMehthodType.QUERY_LIST) {
            parameters.addAll(expander.expand(new ExpansionContext("",
                    new TypeResolver().resolve(autoRequestMethod.getEntityClass()), documentationContext)));
            parameters.addAll(expander.expand(new ExpansionContext("",
                    new TypeResolver().resolve(QueryPageTemplate.class), documentationContext)));
            return parameters;
        } else if (type == AutoRequestMehthodType.SAVE) {
            parameters.addAll(expander.expand(new ExpansionContext("",
                    new TypeResolver().resolve(autoRequestMethod.getEntityClass()), documentationContext)));
        } else if (type == AutoRequestMehthodType.QUERY_BY_ID) {
            parameters.add(new ParameterBuilder().modelRef(new ModelRef("string")).parameterType("path")
                    .type(new TypeResolver().resolve(String.class)).name("id").description("查询id").required(true)
                    .build());
        } else if (type == AutoRequestMehthodType.DELETE_BY_ID) {
            parameters.add(new ParameterBuilder().modelRef(new ModelRef("string")).parameterType("path")
                    .type(new TypeResolver().resolve(String.class)).name("id").description("删除id").required(true)
                    .build());
        } else if (type == AutoRequestMehthodType.UPDATE_BY_ID) {
            parameters.addAll(expander.expand(new ExpansionContext("",
                    new TypeResolver().resolve(autoRequestMethod.getEntityClass()), documentationContext)));
            parameters.add(new ParameterBuilder().modelRef(new ModelRef("string")).parameterType("path")
                    .type(new TypeResolver().resolve(String.class)).name("id").description("更新id").required(true)
                    .build());
        }
        return parameters;
    }

    protected List<springfox.documentation.service.Operation> initOperation(String path,
            AutoRequestMethod autoRequestMethod, DocumentationContext documentationContext) {
        List<Operation> operators = new ArrayList<>();
        Set<RequestMethod> requestMethods = autoRequestMethod.getRequestMappingInfo().getMethodsCondition()
                .getMethods();
        requestMethods.forEach((method) -> {
            String summary = AnnotationUtils.findAnnotation(autoRequestMethod.getMethod(), Explain.class).value();
            operators.add(new OperationBuilder(nameGenerator)
                    .responseMessages(Sets.newHashSet(new ResponseMessageBuilder().code(200).message("OK")
                            .responseModel(new ModelRef("ResultEntityTemplate")).build()))
                    .responseModel(new ModelRef("ResultEntityTemplate")).method(HttpMethod.valueOf(method.toString()))//http请求类型  
                    .produces(autoRequestMethod.getRequestMappingInfo().getProducesCondition().getProducibleMediaTypes()
                            .stream().map(mediaType -> {
                                return mediaType.toString();
                            }).collect(Collectors.toSet()))
                    .summary(summary).notes(summary)//方法描述  
                    .tags(Sets.newHashSet("AutoGeneratorMethod")).deprecated("自动生成方法")
                    .parameters(generatorParameters(autoRequestMethod, documentationContext)).build());
        });
        return operators;
    }

    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        List<ApiDescription> apiDescriptions = new ArrayList<ApiDescription>();
        if (this.map != null) {
            this.map.forEach((path, autoRequestMethodMap) -> {
                autoRequestMethodMap.forEach((method, autoRequestMethod) -> {
                    apiDescriptions.add(new ApiDescription(path, //url  
                            AnnotationUtils.findAnnotation(autoRequestMethod.getMethod(), Explain.class).value(), //描述  
                            initOperation(path, autoRequestMethod, documentationContext), false));
                });
            });
        }
        return apiDescriptions;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}