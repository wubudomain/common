package top.wboost.common.sql.parameter;

import java.util.List;

import top.wboost.common.base.interfaces.Decoder;
import top.wboost.common.util.StringUtil;

public class SqlParameterDecoder implements Decoder<String, String> {

    final String pattern = "\\$\\{-\\{\\?(.*?)\\}-\\}\\$";

    @Override
    public String decode(String sql) {
        List<String> list = StringUtil.getPatternMattcherList(sql, pattern, 1);
        for (String param : list) {
            sql = sql.replaceFirst(pattern, param);
        }
        return sql;
    }

}
