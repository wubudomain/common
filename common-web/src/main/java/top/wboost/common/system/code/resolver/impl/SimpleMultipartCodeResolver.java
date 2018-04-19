package top.wboost.common.system.code.resolver.impl;

import top.wboost.common.base.entity.ResultEntity;

public class SimpleMultipartCodeResolver extends AbstractMultipartCodeResolver {

    @Override
    public ResultEntity resolve(ResultEntity resolveEntity) {
        String message = getMessage(resolveEntity);
        resolveEntity.getInfo().setMessage(message);
        return resolveEntity;
    }

}
