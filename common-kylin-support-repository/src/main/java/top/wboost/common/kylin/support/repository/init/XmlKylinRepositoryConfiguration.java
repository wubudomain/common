package top.wboost.common.kylin.support.repository.init;

import top.wboost.common.context.register.XmlRegisterConfiguration;

public interface XmlKylinRepositoryConfiguration extends XmlRegisterConfiguration {

    public Iterable<String> getBasePackages();

}
