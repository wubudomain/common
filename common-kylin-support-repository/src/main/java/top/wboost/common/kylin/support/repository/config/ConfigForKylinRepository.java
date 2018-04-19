package top.wboost.common.kylin.support.repository.config;

import top.wboost.common.utils.web.utils.PropertiesUtil;

public class ConfigForKylinRepository {

    public static final String PROP_DEFAULT_PROJECT_NAME = "kylin.config.default.project";

    public static final String DEFAULT_PROJECT_NAME = PropertiesUtil.getProperty(PROP_DEFAULT_PROJECT_NAME);

}
