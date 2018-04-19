package top.wboost.common.kylin.support.repository.util;

import java.util.Map;

import top.wboost.common.manager.DefaultWildCardManager;
import top.wboost.common.sql.dialect.DefaultSqlWarp;
import top.wboost.common.sql.manager.DefaultSqlWildCardManager;

/**
 * 通配符统一为 ${-{ 内容{要替换的}内容  }-}$
 * @className WildCardUtil
 * @author jwSun
 * @date 2017年10月25日 上午10:55:42
 * @version 1.0.0
 */
public class WildCardUtil {

    private static DefaultWildCardManager manager = new DefaultSqlWildCardManager(new DefaultSqlWarp());

    public static String replace(String text, Map<String, Object> replaceMap) {
        return manager.replace(text, replaceMap);
    }
}
