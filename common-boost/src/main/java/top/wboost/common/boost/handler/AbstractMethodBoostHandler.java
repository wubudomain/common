package top.wboost.common.boost.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import top.wboost.common.util.StringUtil;

public abstract class AbstractMethodBoostHandler extends AbstractBoostHandler implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter adapter;
    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    protected Map<String, Invoke> invokeMap = new ConcurrentHashMap<>();

    class Invoke {
        public Method method;
        public Object clazz;

        public Invoke(Method method, Object clazz) {
            super();
            this.method = method;
            this.clazz = clazz;
        }

        public Object invoke(HttpServletRequest request)
                throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Map<String, String[]> map = request.getParameterMap();
            String[] names = parameterNameDiscoverer.getParameterNames(this.method);
            List<String> params = new ArrayList<>();
            for (String name : names) {
                String[] strArray = map.get(name);
                if (strArray != null && strArray.length > 0) {
                    params.add(strArray[0]);
                } else {
                    params.add(null);
                }
            }
            return method.invoke(clazz, params.toArray(new String[params.size()]));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
        if (returnValueHandlers != null) {
            this.returnValueHandlers.addHandlers(returnValueHandlers);
        }
    }

    public ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response) {
        String method = getMethodName(request);
        if (StringUtil.notEmpty(method)) {
            Invoke invoke = this.invokeMap.get(method);
            if (invoke != null) {
                try {
                    Object entity = invoke.invoke(request);
                    ModelAndViewContainer mavContainer = new ModelAndViewContainer();
                    MethodParameter returnType = new SynthesizingMethodParameter(invoke.method, -1);
                    returnValueHandlers.handleReturnValue(entity, returnType, mavContainer,
                            new ServletWebRequest(request, response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public abstract String getMethodName(HttpServletRequest request);
}
