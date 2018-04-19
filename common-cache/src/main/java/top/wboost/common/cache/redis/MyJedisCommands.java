package top.wboost.common.cache.redis;

import top.wboost.common.cache.DataCache;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;

public interface MyJedisCommands extends DataCache, JedisCommands, BinaryJedisCommands {

}
