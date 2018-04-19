package top.wboost.common.utils.web.interfaces.context;

import org.springframework.context.event.ContextRefreshedEvent;

public interface EzBootApplicationListener extends EzApplicationListener {

	default public void onWebApplicationEvent(ContextRefreshedEvent event){
    }

    default public void onRootApplicationEvent(ContextRefreshedEvent event) {
    }

}
