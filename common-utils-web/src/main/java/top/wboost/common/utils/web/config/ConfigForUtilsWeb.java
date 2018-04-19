package top.wboost.common.utils.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;

import top.wboost.common.base.ConfigForBase;
import top.wboost.common.utils.web.interfaces.context.EzBootApplicationListener;
import top.wboost.common.utils.web.utils.PropertiesUtil;

//@Configurable
public class ConfigForUtilsWeb implements EzBootApplicationListener {

	@Bean("top.wboost.common.utils.web.utils.PropertiesUtil" + ConfigForBase.SCAN_CONFIG.WEB)
	public PropertiesUtil propertiesUtilForWebApplication() {
		return new PropertiesUtil();
	}

	@Override
	public void onBootApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub

	}

}
