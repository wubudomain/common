package top.wboost.common.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.wboost.common.base.interfaces.Warpper;
import top.wboost.common.system.exception.SystemException;
import top.wboost.common.util.Checker;
import top.wboost.common.util.StringUtil;

/**
 * 通配符统一为 ${-{ 内容{要替换的}内容  }-}$或 {要替换的}
 * <pre>
 * 替换方式:
 * ${-{ 内容{要替换的}内容  }-}$:若没有参数则屏蔽此段内容
 * {要替换的}:若没有参数则抛出异常
 * </pre>
 * @className DefaultWildCardManager
 * @author jwSun
 * @date 2017年9月19日 上午2:37:58
 * @version 1.0.0
 */
public class DefaultWildCardManager implements WildCardManager {

    private static final String SQL_WILDCARD = "\\$\\{-\\{(.*?)\\}-\\}";
    private static final String SQL_PARAM_WILDCARD = "\\{([^\\{]+?)\\}";

    protected Warpper<String> warp;

    public DefaultWildCardManager(Warpper<String> warp) {
        super();
        this.warp = warp;
    }

    public String replace(String text, Map<String, Object> replaceMap) {
        if (replaceMap == null) {
            replaceMap = new HashMap<>();
        }
        Checker.hasText(text, "WildCardUtil:text is null");
        List<String> replaceList = StringUtil.getPatternMattcherList(text, SQL_WILDCARD, 1);
        for (String replaceOne : replaceList) {
            String newStr = replaceOne;
            List<String> oneList = StringUtil.getPatternMattcherList(replaceOne, SQL_PARAM_WILDCARD, 1);
            if (oneList.size() == 0) {
                throw new SystemException();
            }
            int replaceCount = 0;
            for (String one : oneList) {
                Object replace = replaceMap.get(one);
                if (replace != null) {
                    // newStr = newStr.replaceAll(SQL_PARAM_WILDCARD, warp.warp(replace));
                    newStr = newStr.replace("{" + one + "}", warp.warp(replace));
                    replaceCount++;
                }
            }
            String realReplace;
            if (replaceCount != oneList.size()) {
                realReplace = "";
            } else {
                realReplace = newStr;
            }
            text = text.replace("${-{" + replaceOne + "}-}$", realReplace);
        }
        List<String> replaceParamsList = StringUtil.getPatternMattcherList(text, SQL_PARAM_WILDCARD, 1);
        for (String replaceParam : replaceParamsList) {
            Object replace = replaceMap.get(replaceParam);
            if (replace != null) {
                text = text.replace("{" + replaceParam + "}", warp.warp(replace));
            } else {
                throw new SystemException("replace: name is " + replaceParam + ",cant get a value");
            }
        }
        return text;
    }
}
