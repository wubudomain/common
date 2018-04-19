package top.wboost.common.utils.web.interfaces.context;

import java.util.Collection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import top.wboost.common.utils.web.utils.SpringApplicationUtil;

public interface EzApplicationListenerManager extends ApplicationListener<ApplicationEvent> {

	default void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextClosedEvent) {
			onContextClosedEvent((ContextClosedEvent) event);
		} else if (event instanceof ContextRefreshedEvent) {
			onContextRefreshedEvent((ContextRefreshedEvent) event);
		} else if (event instanceof ContextStartedEvent) {
			onContextStartedEvent((ContextStartedEvent) event);
		} else if (event instanceof ContextStoppedEvent) {
			onContextStoppedEvent((ContextStoppedEvent) event);
		} else {
			// TODO
		}
	}

	default void onContextStoppedEvent(ContextStoppedEvent event) {

	}

	default void onContextStartedEvent(ContextStartedEvent event) {

	}

	default void onContextClosedEvent(ContextClosedEvent event) {

	}

	default void onContextRefreshedEvent(ContextRefreshedEvent event) {
		ApplicationContext parentContext = event.getApplicationContext().getParent();
		for (EzApplicationListener ezApplicationListener : getEzApplicationListeners(event.getApplicationContext())) {
			if (SpringApplicationUtil.isBootApplicationContext(event)) {
				// spring boot
				ezApplicationListener.onBootApplicationEvent(event);
				if (ezApplicationListener.doWebAndRootApplicationListener(event)) {
					ezApplicationListener.onWebApplicationEvent(event);
					ezApplicationListener.onRootApplicationEvent(event);
				}
			} else {
				// no spring boot
				if (parentContext != null) {
					ezApplicationListener.onWebApplicationEvent(event);
				} else {
					ezApplicationListener.onRootApplicationEvent(event);
				}
			}
		}
	}

	Collection<EzApplicationListener> getEzApplicationListeners(ApplicationContext applicationContext);

	void setEzApplicationListeners(Collection<EzApplicationListener> ezApplicationListeners);

}
