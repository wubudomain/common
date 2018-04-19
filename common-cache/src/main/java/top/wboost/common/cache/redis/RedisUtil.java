package top.wboost.common.cache.redis;

import java.lang.reflect.Proxy;

import org.slf4j.Logger;

import top.wboost.common.cache.redis.core.RedisClientTemplate;
import top.wboost.common.cache.redis.core.RedisInvocationHandler;
import top.wboost.common.cache.redis.dataSource.RedisDataSource;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.utils.web.utils.SpringBeanUtil;

/**
 * redis工具类
 * 
 * @author jwSun
 * @date 2017年3月31日 下午4:28:01
 */
public class RedisUtil {

    private static Logger log = LoggerUtil.getLogger(RedisUtil.class);

    private static MyJedisCommands jedisCommands = null;

    static {
        try {
            RedisDataSource redisDataSource = (RedisDataSource) SpringBeanUtil.getBean("redisDataSource");
            jedisCommands = (MyJedisCommands) Proxy.newProxyInstance(RedisClientTemplate.class.getClassLoader(),
                    RedisClientTemplate.class.getInterfaces(), new RedisInvocationHandler(redisDataSource));
        } catch (Exception e) {
            log.error("redis服务未启动", e);
        }
    }

    /**
     * 获取操作命令模版
     * 
     * @author jwSun
     * @date 2016年12月10日下午1:55:36
     * @return
     */
    public static MyJedisCommands getJedisCommands() {
        return jedisCommands;
    }

    /**
     * redis服务是否存在
     * 
     * @author jwSun
     * @date 2016年12月10日下午1:55:26
     * @return
     */
    public static Boolean serverExits() {
        if (jedisCommands == null) {
            log.error("server not Exits");
            throw new RuntimeException("server not Exits");
        }
        return true;
    }
}
