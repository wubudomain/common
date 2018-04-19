package top.wboost.common.utils.web.interfaces.context;

import org.springframework.context.event.ContextRefreshedEvent;

public interface EzWebApplicationListener extends EzApplicationListener {

    public void onWebApplicationEvent(ContextRefreshedEvent event);

    default public void onRootApplicationEvent(ContextRefreshedEvent event) {
    }
    
    default public void onBootApplicationEvent(ContextRefreshedEvent event) {
    }

}
