package top.wboost.common.boost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.context.SupportXmlWebApplicationContext;
import top.wboost.common.system.code.SystemCode;
import top.wboost.common.util.ResponseUtil;
import top.wboost.common.utils.web.utils.HtmlUtil;

/**
 * springmvc servlet 扩展
 * @className DispatcherServlet
 * @author jwSun
 * @date 2017年9月24日 下午7:51:37
 * @version 1.0.0
 */
public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet {

    private static final long serialVersionUID = -2067077725269764849L;

    /**
     * 404处理
     */
    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(404);
        String result = ResponseUtil
                .codeResolveJson(ResultEntity.fail(SystemCode.NO_PAGE).setData(request.getRequestURI()).build());
        HtmlUtil.writerJson(response, result);
    }
    
    /**
     * 使用自定义扩展上下文环境
     */
    public DispatcherServlet() {
        setContextClass(SupportXmlWebApplicationContext.class);
    }
}
