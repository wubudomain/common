package top.wboost.common.spring.boot.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.wboost.common.annotation.Explain;
import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.base.page.QueryPage;
import top.wboost.common.spring.boot.swagger.template.QueryPageTemplate;
import top.wboost.common.spring.boot.swagger.template.ResultEntityTemplate;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                /**apiInfo**/
                .apiInfo(apiInfo())
                /**useDefaultResponseMessages**/
                .useDefaultResponseMessages(false)
                /**alternateTypeRules**/
                .alternateTypeRules(AlternateTypeRules.newRule(QueryPage.class, QueryPageTemplate.class))
                .alternateTypeRules(AlternateTypeRules.newRule(ResultEntity.class, ResultEntityTemplate.class))
                /**select**/
                .select()
                /**apis**/
                .apis(RequestHandlerSelectors.withMethodAnnotation(Explain.class))
                /**paths**/
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("quick plugin").description("quick plugin")
                .termsOfServiceUrl("http://www.wboost.top/").version("1.0").build();
    }

}