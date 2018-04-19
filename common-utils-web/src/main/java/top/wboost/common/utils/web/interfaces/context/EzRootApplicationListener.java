package top.wboost.common.utils.web.interfaces.context;

import org.springframework.context.event.ContextRefreshedEvent;

public interface EzRootApplicationListener extends EzApplicationListener {

    public void onRootApplicationEvent(ContextRefreshedEvent event);

    default public void onWebApplicationEvent(ContextRefreshedEvent event) {
    }
    
    default public void onBootApplicationEvent(ContextRefreshedEvent event) {
    }

}
