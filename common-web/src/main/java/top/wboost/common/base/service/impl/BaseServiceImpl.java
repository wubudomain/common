package top.wboost.common.base.service.impl;

import top.wboost.common.base.service.BaseService;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.util.ReflectUtil;

/**
 * Service公共实现基础类
 * @className BaseServiceImpl
 * @author jwSun
 * @date 2017年6月28日 下午8:10:40
 * @version 1.0.0
 * @param <T>
 */
public abstract class BaseServiceImpl<T, ID extends java.io.Serializable> implements BaseService<T, ID> {

    protected Logger log = LoggerUtil.getLogger(getClass());

    // ***************************自定义方法********************************//

    @SuppressWarnings("unchecked")
    protected Class<T> getThisClass() {
        return (Class<T>) ReflectUtil.getGenericInterfaces(getClass(), 0);
    }

}
