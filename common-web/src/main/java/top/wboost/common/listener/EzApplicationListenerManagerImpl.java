package top.wboost.common.listener;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import top.wboost.common.base.annotation.AutoRootApplicationConfig;
import top.wboost.common.utils.web.interfaces.context.EzApplicationListener;

@AutoRootApplicationConfig
public class EzApplicationListenerManagerImpl
		implements top.wboost.common.utils.web.interfaces.context.EzApplicationListenerManager {

	@Override
	public Collection<EzApplicationListener> getEzApplicationListeners(ApplicationContext context) {
		Map<String, EzApplicationListener> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
				EzApplicationListener.class, true, false);
		return matchingBeans.values();
	}

	@Override
	public void setEzApplicationListeners(Collection<EzApplicationListener> ezApplicationListeners) {
		// TODO Auto-generated method stub

	}

}
