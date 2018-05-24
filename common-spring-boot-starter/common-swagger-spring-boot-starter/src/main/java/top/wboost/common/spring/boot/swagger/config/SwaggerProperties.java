package top.wboost.common.spring.boot.swagger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import top.wboost.base.spring.boot.starter.GlobalForSpringBootStarter;

@Data
@ConfigurationProperties(GlobalForSpringBootStarter.PROPERTIES_PREFIX + "swagger")
public class SwaggerProperties {

    private String title;
    private String description;
    private String termsOfServiceUrl;
    private String version;

}
