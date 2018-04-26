package top.wboost.common.spring.boot.webmvc;

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
import top.wboost.common.base.page.QueryPage;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                /**apiInfo**/
                .apiInfo(apiInfo())
                /**useDefaultResponseMessages**/
                .useDefaultResponseMessages(false)
                /**alternateTypeRules**/
                .alternateTypeRules(AlternateTypeRules.newRule(QueryPage.class, QueryPageTemplate.class))
                /**genericModelSubstitutes**/
                //.genericModelSubstitutes(ResultEntity.class)
                /**select**/
                .select()
                /**apis**/
                .apis(RequestHandlerSelectors.withMethodAnnotation(Explain.class))
                /**paths**/
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("测试").description("详情").termsOfServiceUrl("http://www.wboost.top/")
                .version("1.0").build();
    }

}