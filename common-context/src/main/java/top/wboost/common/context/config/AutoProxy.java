package top.wboost.common.context.config;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 代理类接口
 * 实现此接口将生成要代理的接口实现类并调用此实现类的invoke方法
 * @className AutoProxy
 * @author jwSun
 * @date 2017年12月5日 下午6:54:07
 * @version 1.0.0
 */
public interface AutoProxy extends MethodInterceptor {

    /**
     * 获得实现类实例
     * @param clazz 要创建实例的接口类
     * @param config xml中配置
     * @return AutoProxy 代理实现类
     * @throws Exception 创建实现类失败
     */
    default public AutoProxy getObject(Class<?> clazz, Map<String, Object> config) throws Exception {
        return this.getClass().newInstance();
    }

}
