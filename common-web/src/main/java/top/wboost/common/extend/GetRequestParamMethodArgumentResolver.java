package top.wboost.common.extend;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import top.wboost.common.base.enums.CharsetEnum;

/**
 * 解决get方式传递参数 中文会乱码问题(tomcat7默认get编码iso-8859-1 tomcat8默认为utf-8)
 * @author jwSun
 * @date 2017年3月28日 下午5:13:02
 */
public class GetRequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getGenericParameterType() != java.lang.String.class) {
            return false;
        }
        RequestMapping requestMapping = parameter.getMethod().getAnnotation(RequestMapping.class);

        if (requestMapping != null) {
            RequestMethod[] rms = requestMapping.method();
            if (rms != null) {
                for (RequestMethod rm : rms) {
                    if (rm.name().equals(RequestMethod.GET.toString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Object arg = null;
        String[] paramValues = webRequest.getParameterValues(parameter.getParameterName());
        if (paramValues != null) {
            arg = paramValues.length == 1 ? paramValues[0] : paramValues;
        }
        if (arg != null) {
            return new String(arg.toString().getBytes(CharsetEnum.ISO_8859_1.getName()), CharsetEnum.UTF_8.getName());
        }
        return null;
    }

}
