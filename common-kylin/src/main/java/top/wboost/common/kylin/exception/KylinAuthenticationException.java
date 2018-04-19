package top.wboost.common.kylin.exception;

import top.wboost.common.exception.BusinessCodeException;
import top.wboost.common.system.code.CodeMessageManager;

/**
 * kylin权限验证失败异常
 * @className KylinAuthenticationException
 * @author jwSun
 * @date 2017年7月27日 下午3:52:14
 * @version 1.0.0
 */
public class KylinAuthenticationException extends BusinessCodeException implements KylinException {

    private final static int businessCode = CodeMessageManager.NO_MESSAGE_CODE;
    private final static String PROMPT = "kylin权限验证失败";

    private static final long serialVersionUID = 4080810070315357155L;

    public KylinAuthenticationException() {
        super(businessCode, PROMPT);
    }

    public KylinAuthenticationException(String message) {
        super(businessCode, PROMPT + ":" + message);
    }
}
