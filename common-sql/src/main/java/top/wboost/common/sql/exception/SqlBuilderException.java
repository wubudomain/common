package top.wboost.common.sql.exception;

import top.wboost.common.system.code.SystemCode;
import top.wboost.common.system.exception.SystemCodeException;

public class SqlBuilderException extends SystemCodeException {

    private static final long serialVersionUID = 1L;

    public SqlBuilderException(String message) {
        super(SystemCode.CHECK_FAIL, message);
    }

}
