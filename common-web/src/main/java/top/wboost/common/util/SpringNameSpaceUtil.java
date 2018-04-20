package top.wboost.common.util;

import java.util.List;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.system.code.SystemCode;
import top.wboost.common.utils.web.utils.PropertiesUtil;

public class SpringNameSpaceUtil {

    private static Logger log = LoggerUtil.getLogger(SpringNameSpaceUtil.class);

    // 匹配${}
    private static final String PATTERN_COMPILE = "\\$\\{(.*?)\\}";
    private static String patternComolie = PATTERN_COMPILE;

    //默认配置文件地址
    private static final String DEFAULT_PROPERTIES = "properties/spring-wboost-handler-config.properties";
    private static final String OWN_DEFAULT_PROPERTIES = "properties/wboost-spring-config.properties";

    /**
     * 解析通配符
     * @param element
     * @param parser
     * @param attributeName
     */
    public static void resolveWildcard(Element element, ParserContext parserContext, String attributeName) {
        String basePackage = element.getAttribute(attributeName);
        List<String> list = StringUtil.getPatternMattcherList(basePackage, patternComolie, 1);
        for (String converter : list) {
            String value = PropertiesUtil.getProperty(converter);
            if (!StringUtil.notEmpty(value)) {
                value = PropertiesUtil.getProperty(converter, DEFAULT_PROPERTIES);
            }
            if (!StringUtil.notEmpty(value)) {
                value = PropertiesUtil.getProperty(converter, OWN_DEFAULT_PROPERTIES);
            }
            if (StringUtil.notEmpty(value)) {
                basePackage = basePackage.replace("${" + converter + "}", value);
            }
        }
        if (StringUtil.getPatternMattcherList(basePackage, patternComolie, 1).size() > 0) {
            log.error(ResponseUtil
                    .codeResolveJson(ResultEntity.fail(SystemCode.WBOOST_HANDLER_BASEPACKAGE_RESOLVE_ERROR).build()));
            //由于编辑器会有烦人的提示，所以暂时注释，有必要时再开启
            //throw new SystemCodeException(SystemCode.WBOOST_HANDLER_BASEPACKAGE_RESOLVE_ERROR,
            //        "resolve error:" + basePackage);
        }
        element.setAttribute(attributeName, basePackage);
    }

}
