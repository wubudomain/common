package top.wboost.common.system.spring.init.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.event.ContextRefreshedEvent;

import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.system.spring.init.interfaces.SpringInitedInvoke;
import top.wboost.common.utils.web.interfaces.context.EzRootApplicationListener;
import top.wboost.common.utils.web.utils.SpringBeanUtil;

/**
 * 调用指定spring bean方法,需要类实现{@link top.wboost.system.spring.init.interfaces.SpringInitedInvoke}
  <pre>
       !!! 需要在spring配置文件中指定执行器!!!
    &lt;bean class="top.wboost.system.spring.init.impl.AbstractSpringInitedInvoke"&gt;
        &lt;property name="invokeMap"&gt;
            &lt;map&gt;
                &lt;entry key="top.wboost.common.util.PropertiesUtil" value="log4j.properties"/&gt;
            &lt;/map&gt;
        &lt;/property&gt;
    &lt;/bean&gt;
  </pre>
 * @see top.wboost.system.spring.init.interfaces.SpringInitedInvoke
 * @author jwSun
 * @date 2017年6月23日 下午3:13:58
 */
public class AbstractSpringInitedInvoke implements EzRootApplicationListener {

    private Logger log = LoggerUtil.getLogger(getClass());

    /**调用执行map key:类全路径,如com.xx.xx value:执行方法参数**/
    private Map<String, Object> invokeMap;

    @Override
    public String toString() {
        return "AbstractSpringInitedInvoke [invokeMap=" + invokeMap + "]";
    }

    public Map<String, Object> getInvokeMap() {
        return invokeMap;
    }

    public void setInvokeMap(Map<String, Object> invokeMap) {
        this.invokeMap = invokeMap;
    }

    public void onRootApplicationEvent(ContextRefreshedEvent event) {
        if (SpringBeanUtil.getApplicationContext() == null) {
            throw new RuntimeException("SpringBeanUtil ApplicationContext 不存在，请检查SpringBeanUtil是否配置");
        }
        for (Entry<String, Object> entry : invokeMap.entrySet()) {
            try {
                SpringInitedInvoke springInitedInvoke = (SpringInitedInvoke) SpringBeanUtil
                        .getBean(Class.forName(entry.getKey()));
                if (springInitedInvoke == null) {
                    throw new RuntimeException("未获得:" + entry.getKey());
                }
                springInitedInvoke.apply(entry.getValue());
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }
    }
}
