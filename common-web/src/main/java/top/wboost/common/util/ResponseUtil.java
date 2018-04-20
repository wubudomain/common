package top.wboost.common.util;

import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.system.code.resolver.CodeResolver;
import top.wboost.common.system.code.resolver.impl.SimpleMultipartCodeResolver;
import top.wboost.common.utils.web.utils.JSONObjectUtil;

public class ResponseUtil {

    private static CodeResolver codeResolver = new SimpleMultipartCodeResolver();

    public static ResultEntity codeResolve(ResultEntity resolveEntity) {
        if (resolveEntity.getInfo().getSystemCode() != null) {
            return resolveEntity.getInfo().getSystemCode().getCodeResolver().resolve(resolveEntity);
        }
        return codeResolver.resolve(resolveEntity);
    }

    public static String codeResolveJson(ResultEntity resolveEntity) {
        ResultEntity resolve = codeResolve(resolveEntity);
        return JSONObjectUtil.toJSONString(resolve, resolveEntity.getJsonConfig(), resolveEntity.getFilterNames());
    }

    public static Object codePrompt(ResultEntity resolveEntity) {
        return resolveEntity.getInfo().getSystemCode().getCodeResolver().resolve(resolveEntity);
    }
}
