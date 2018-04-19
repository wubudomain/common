package top.wboost.common.boost.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;

import top.wboost.common.annotation.Explain;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;

public abstract class AbstractBoostHandler implements BoostHandler {

    protected Logger log = LoggerUtil.getLogger(getClass());

    protected AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    @Explain(exceptionCode = 0, value = "boost")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("boostHandler handle...");
        }
        return handleInternal(request, response);
    }

    /**
     * 执行sql操作
     * @param request
     * @param response
     * @return
     */
    @Explain(exceptionCode = 0, value = "boost")
    public abstract ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response);
}
