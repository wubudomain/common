package top.wboost.common.kylin.support.repository.exception;

import top.wboost.common.system.exception.SystemException;

public class KylinRepositoryNoProjectException extends SystemException {

    private static final long serialVersionUID = 46740241370420152L;

    private static String prompt = "KylinRepository注解中必须有project或cubeName！";

    public KylinRepositoryNoProjectException(String type) {
        super(prompt + ",type:" + type);
    }

}
