package top.wboost.common.spring.boot.swagger;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.wboost.common.annotation.Explain;
import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.base.page.QueryPage;
import top.wboost.common.base.restful.AutoRequestMehthodType;
import top.wboost.common.spring.boot.swagger.config.SwaggerProperties;
import top.wboost.common.spring.boot.swagger.template.QueryPageTemplate;
import top.wboost.common.spring.boot.swagger.template.ResultEntityTemplate;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class Swagger2Config {

    @Autowired
    SwaggerProperties swaggerProperties;

    @Bean
    public Docket createRestApi() {
        Set<String> ignoreNames = AutoRequestMehthodType.getAllNames();
        return new Docket(DocumentationType.SWAGGER_2)
                /**apiInfo**/
                .apiInfo(apiInfo())
                /**useDefaultResponseMessages**/
                .useDefaultResponseMessages(false)
                /**alternateTypeRules**/
                .alternateTypeRules(AlternateTypeRules.newRule(QueryPage.class, QueryPageTemplate.class),
                        AlternateTypeRules.newRule(ResultEntity.class, ResultEntityTemplate.class))
                /**select**/
                .select()
                /**apis**/
                .apis((input) -> {
                    return input.isAnnotatedWith(Explain.class) && input.getHandlerMethod()
                            .getBeanType() != top.wboost.common.base.restful.AutoRequestMethodInvoke.class;
                })
                /**paths**/
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl()).version(swaggerProperties.getVersion())
                .build();
    }

}