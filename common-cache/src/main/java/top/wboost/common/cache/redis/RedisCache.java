package top.wboost.common.cache.redis;

import top.wboost.common.cache.WebCache;

public class RedisCache implements WebCache {

    @Override
    public Object getCache() {
        return RedisUtil.getJedisCommands();
    }

}
