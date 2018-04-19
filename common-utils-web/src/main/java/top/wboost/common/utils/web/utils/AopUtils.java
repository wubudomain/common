package top.wboost.common.utils.web.utils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

public class AopUtils extends org.springframework.aop.support.AopUtils {
    /** 
     * 获取代理类最终代理对象
     * @param proxy 代理对象 
     * @return  
     * @throws Exception 
     */
    public static Object getTarget(Object proxy) {
        try {
            if (!AopUtils.isAopProxy(proxy)) {
                return proxy;//不是代理对象  
            }
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                proxy = getJdkDynamicProxyTargetObject(proxy);
            } else {
                proxy = getCglibProxyTargetObject(proxy);
            }
            return getTarget(proxy);
        } catch (Exception e) {
            return null;
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }

    public static void main(String[] args) {
        String t = DateUtil.getDateTimeFormatter("yyyyMMddHHmmss").format(LocalDateTime.now().plusMinutes(-360));
        System.out.println(t);
    }
}
