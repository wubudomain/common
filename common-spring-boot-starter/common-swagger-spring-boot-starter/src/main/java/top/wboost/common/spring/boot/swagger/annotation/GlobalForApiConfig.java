package top.wboost.common.spring.boot.swagger.annotation;

import top.wboost.base.spring.boot.starter.GlobalForSpringBootStarter;
import top.wboost.common.utils.web.utils.PropertiesUtil;

public class GlobalForApiConfig {

    public static final String DEFAULT_VERSION_SHOW = "NO_VERSION";

    public static final String DEFAULT_VERSION = PropertiesUtil
            .getPropertyOrDefault(GlobalForSpringBootStarter.PROPERTIES_PREFIX + "api.version", DEFAULT_VERSION_SHOW);

}
