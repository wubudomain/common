package top.wboost.common.boost.handler;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.ResponseBody;

import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.utils.web.utils.AopUtils;

@ResponseBody
public abstract class ResultEntityMethodBoostHandler extends AbstractMethodBoostHandler implements InitializingBean {

    private final String URL_PREFIX = "/boost/method/";

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Object obj = AopUtils.getTarget(this);
        for (Method m : obj.getClass().getDeclaredMethods()) {
            if (m.getReturnType() == ResultEntity.class) {
                Invoke invoke = new Invoke(m, this);
                this.invokeMap.put(m.getName(), invoke);
            }
        }
    }

    @Override
    public String getUrlMapping() {
        String suffix = getUrlSuffix();
        String urlMapping;
        if (suffix.startsWith("/")) {
            urlMapping = URL_PREFIX + suffix.substring(1);
        } else {
            urlMapping = URL_PREFIX + suffix;
        }
        return urlMapping;
    }

    /**
     * 如 /demo/{abc}
     * @return
     */
    public abstract String getUrlSuffix();

    /**
     * 如abc
     * @return
     */
    public abstract String getMethodVariable();

    @Override
    public String getMethodName(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = pathMatcher.extractUriTemplateVariables(getUrlMapping(), path).get(getMethodVariable());
        return method;
    }

}
