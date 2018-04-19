package top.wboost.common.cache.redis;

import top.wboost.common.cache.CacheManager;
import top.wboost.common.cache.DataCache;

public class RedisCacheManager implements CacheManager {

    @Override
    public DataCache getCache() {
        return RedisUtil.getJedisCommands();
    }

}
