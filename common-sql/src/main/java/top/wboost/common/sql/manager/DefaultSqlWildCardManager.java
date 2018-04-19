package top.wboost.common.sql.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import top.wboost.common.base.annotation.AutoRootApplicationConfig;
import top.wboost.common.manager.DefaultWildCardManager;
import top.wboost.common.sql.dialect.SqlWarp;
import top.wboost.common.system.exception.SystemException;
import top.wboost.common.util.Checker;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.utils.PropertiesUtil;

/**
 * 通配符统一为 ${-{ 内容{要替换的}内容  }-}
 * @className DefaultWildCardManager
 * @author jwSun
 * @date 2017年9月19日 上午2:37:58
 * @version 1.0.0
 */
@AutoRootApplicationConfig("defaultSqlWildCardManager")
public class DefaultSqlWildCardManager extends DefaultWildCardManager {

    @Autowired
    public DefaultSqlWildCardManager(@Qualifier("defaultSqlWarp") SqlWarp warp) {
        super(warp);
    }

    private static final String SQL_WILDCARD = "\\$\\{-\\{(.*?)\\}-\\}";
    private static final String SQL_PARAM_WILDCARD = "\\{([^\\{]+?)\\}";

    //匹配标准sql字段
    private static final String SQL_FIELDS_WILDCARD = "([^ ]*?) (AS|as) (.*?)(, | from| FROM)";

    public String replace(String text, Map<String, Object> replaceMap) {
        if (replaceMap == null) {
            replaceMap = new HashMap<>();
        }
        Checker.hasText(text, "WildCardUtil:text is null");
        List<String> replaceParamsListP = StringUtil.getPatternMattcherList(text, "\\{<P>(.*?)</P>\\}", 1);
        for (String replaceParam : replaceParamsListP) {
            String replace = PropertiesUtil.getProperty(replaceParam);
            if (replace != null) {
                text = text.replace("{<P>" + replaceParam + "</P>}", replace);
            } else {
                throw new SystemException("replace: name is " + replaceParam + ",cant get a value");
            }
        }
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
                    String warpStr;
                    if (one.startsWith("addSql")) {
                        warpStr = replace.toString();
                    } else {
                        warpStr = warp.warp(replace);
                    }
                    newStr = replaceOne.replace("{" + one + "}", warpStr);
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
                String warpStr;
                if (replaceParam.startsWith("addSql")) {
                    warpStr = replace.toString();
                } else {
                    warpStr = warp.warp(replace);
                }
                text = text.replace("{" + replaceParam + "}", warpStr);
            } else {
                throw new SystemException("replace: name is " + replaceParam + ",cant get a value");
            }
        }
        return text;
    }

    /**
     * 自动获得简单sql语句字段 必须有as 如  select xx as xxx ,会识别为xxx 必须写标准的sql
     * @param sql 简单sql语句
     * @return
     */
    public List<String> getAutoFieldsNames(String sql) {
        String formatSql = StringUtil.format(sql);
        List<String> list = StringUtil.getPatternMattcherList(formatSql, SQL_FIELDS_WILDCARD, 3);
        List<String> retList = new ArrayList<>();
        for (String one : list) {
            retList.add(one.trim());
        }
        return retList;
    }
}
