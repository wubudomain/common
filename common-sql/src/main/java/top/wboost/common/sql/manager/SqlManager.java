package top.wboost.common.sql.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.wboost.common.sql.parameter.SqlParameterDecoder;
import top.wboost.common.sql.parameter.SqlParameterEncoder;
import top.wboost.common.sql.warp.DefaultSqlWarp;
import top.wboost.common.sql.warp.SqlWarp;
import top.wboost.common.utils.web.utils.SpringBeanUtil;

public class SqlManager {

    private static SqlParameterDecoder decoder = new SqlParameterDecoder();
    private static SqlParameterEncoder encoder = new SqlParameterEncoder();
    private static SqlWarp sqlWarp = new DefaultSqlWarp();

    public static SqlWarp getSqlWarp() {
        SqlWarp sqlWarp = SpringBeanUtil.getBean(SqlWarp.class);
        if (sqlWarp == null) {
            sqlWarp = SqlManager.sqlWarp;
        }
        return sqlWarp;
    }

    public static SqlParameterEncoder getEncoder() {
        SqlParameterEncoder encoder = SpringBeanUtil.getBean(SqlParameterEncoder.class);
        if (encoder == null) {
            encoder = SqlManager.encoder;
        }
        return encoder;
    }

    public static SqlParameterDecoder getDecoder() {
        SqlParameterDecoder decoder = SpringBeanUtil.getBean(SqlParameterDecoder.class);
        if (decoder == null) {
            decoder = SqlManager.decoder;
        }
        return decoder;
    }

    public static String encode(String value) {
        return getEncoder().encode(value);
    }

    public static String[] encode(String[] value) {
        List<String> strs = new ArrayList<>();
        Arrays.asList(value).forEach(str -> {
            strs.add(encode(str));
        });
        return strs.toArray(new String[strs.size()]);
    }

    public static String decode(String value) {
        return getDecoder().decode(value);
    }

    public static String warp(String value) {
        return getDecoder().decode(value);
    }

}
