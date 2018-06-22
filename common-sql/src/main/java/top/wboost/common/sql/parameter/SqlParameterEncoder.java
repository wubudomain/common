package top.wboost.common.sql.parameter;

import top.wboost.common.base.interfaces.Encoder;

public class SqlParameterEncoder implements Encoder<String, String> {

    @Override
    public String encode(String value) {
        return "({?" + value + "})";
    }

}
