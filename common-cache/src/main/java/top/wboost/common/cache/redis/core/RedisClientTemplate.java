package top.wboost.common.cache.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import top.wboost.common.cache.redis.MyJedisCommands;

/**
 * redis发送模版,使用动态代理调用官方所有方法,没有实际方法
 * ClassName: RedisClientTemplate 
 * @author jwSun
 * @date 2016年12月10日下午1:47:41
 */
public class RedisClientTemplate implements MyJedisCommands {

    @Override
    public Long append(String arg0, String arg1) {

        return null;
    }

    @Override
    public Long bitcount(String arg0) {

        return null;
    }

    @Override
    public Long bitcount(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public List<Long> bitfield(String arg0, String... arg1) {

        return null;
    }

    @Override
    public Long bitpos(String arg0, boolean arg1) {

        return null;
    }

    @Override
    public Long bitpos(String arg0, boolean arg1, BitPosParams arg2) {

        return null;
    }

    @Override
    public List<String> blpop(String arg0) {

        return null;
    }

    @Override
    public List<String> blpop(int arg0, String arg1) {

        return null;
    }

    @Override
    public List<String> brpop(String arg0) {

        return null;
    }

    @Override
    public List<String> brpop(int arg0, String arg1) {

        return null;
    }

    @Override
    public Long decr(String arg0) {

        return null;
    }

    @Override
    public Long decrBy(String arg0, long arg1) {

        return null;
    }

    @Override
    public Long del(String arg0) {

        return null;
    }

    @Override
    public String echo(String arg0) {

        return null;
    }

    @Override
    public Boolean exists(String arg0) {

        return null;
    }

    @Override
    public Long expire(String arg0, int arg1) {

        return null;
    }

    @Override
    public Long expireAt(String arg0, long arg1) {

        return null;
    }

    @Override
    public Long geoadd(String arg0, Map<String, GeoCoordinate> arg1) {

        return null;
    }

    @Override
    public Long geoadd(String arg0, double arg1, double arg2, String arg3) {

        return null;
    }

    @Override
    public Double geodist(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Double geodist(String arg0, String arg1, String arg2, GeoUnit arg3) {

        return null;
    }

    @Override
    public List<String> geohash(String arg0, String... arg1) {

        return null;
    }

    @Override
    public List<GeoCoordinate> geopos(String arg0, String... arg1) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(String arg0, double arg1, double arg2, double arg3, GeoUnit arg4) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(String arg0, double arg1, double arg2, double arg3, GeoUnit arg4,
            GeoRadiusParam arg5) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String arg0, String arg1, double arg2, GeoUnit arg3) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String arg0, String arg1, double arg2, GeoUnit arg3,
            GeoRadiusParam arg4) {

        return null;
    }

    @Override
    public String get(String arg0) {

        return null;
    }

    @Override
    public String getSet(String arg0, String arg1) {

        return null;
    }

    @Override
    public Boolean getbit(String arg0, long arg1) {

        return null;
    }

    @Override
    public String getrange(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long hdel(String arg0, String... arg1) {

        return null;
    }

    @Override
    public Boolean hexists(String arg0, String arg1) {

        return null;
    }

    @Override
    public String hget(String arg0, String arg1) {

        return null;
    }

    @Override
    public Map<String, String> hgetAll(String arg0) {

        return null;
    }

    @Override
    public Long hincrBy(String arg0, String arg1, long arg2) {

        return null;
    }

    @Override
    public Double hincrByFloat(String arg0, String arg1, double arg2) {

        return null;
    }

    @Override
    public Set<String> hkeys(String arg0) {

        return null;
    }

    @Override
    public Long hlen(String arg0) {

        return null;
    }

    @Override
    public List<String> hmget(String arg0, String... arg1) {

        return null;
    }

    @Override
    public String hmset(String arg0, Map<String, String> arg1) {

        return null;
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String arg0, int arg1) {

        return null;
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String arg0, String arg1) {

        return null;
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String arg0, String arg1, ScanParams arg2) {

        return null;
    }

    @Override
    public Long hset(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Long hsetnx(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public List<String> hvals(String arg0) {

        return null;
    }

    @Override
    public Long incr(String arg0) {

        return null;
    }

    @Override
    public Long incrBy(String arg0, long arg1) {

        return null;
    }

    @Override
    public Double incrByFloat(String arg0, double arg1) {

        return null;
    }

    @Override
    public String lindex(String arg0, long arg1) {

        return null;
    }

    @Override
    public Long linsert(String arg0, LIST_POSITION arg1, String arg2, String arg3) {

        return null;
    }

    @Override
    public Long llen(String arg0) {

        return null;
    }

    @Override
    public String lpop(String arg0) {

        return null;
    }

    @Override
    public Long lpush(String arg0, String... arg1) {

        return null;
    }

    @Override
    public Long lpushx(String arg0, String... arg1) {

        return null;
    }

    @Override
    public List<String> lrange(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long lrem(String arg0, long arg1, String arg2) {

        return null;
    }

    @Override
    public String lset(String arg0, long arg1, String arg2) {

        return null;
    }

    @Override
    public String ltrim(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long move(String arg0, int arg1) {

        return null;
    }

    @Override
    public Long persist(String arg0) {

        return null;
    }

    @Override
    public Long pexpire(String arg0, long arg1) {

        return null;
    }

    @Override
    public Long pexpireAt(String arg0, long arg1) {

        return null;
    }

    @Override
    public Long pfadd(String arg0, String... arg1) {

        return null;
    }

    @Override
    public long pfcount(String arg0) {

        return 0;
    }

    @Override
    public String psetex(String arg0, long arg1, String arg2) {

        return null;
    }

    @Override
    public Long pttl(String arg0) {

        return null;
    }

    @Override
    public String rpop(String arg0) {

        return null;
    }

    @Override
    public Long rpush(String arg0, String... arg1) {

        return null;
    }

    @Override
    public Long rpushx(String arg0, String... arg1) {

        return null;
    }

    @Override
    public Long sadd(String arg0, String... arg1) {

        return null;
    }

    @Override
    public Long scard(String arg0) {

        return null;
    }

    @Override
    public String set(String arg0, String arg1) {

        return null;
    }

    @Override
    public String set(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public String set(String arg0, String arg1, String arg2, String arg3, long arg4) {

        return null;
    }

    @Override
    public Boolean setbit(String arg0, long arg1, boolean arg2) {

        return null;
    }

    @Override
    public Boolean setbit(String arg0, long arg1, String arg2) {

        return null;
    }

    @Override
    public String setex(String arg0, int arg1, String arg2) {

        return null;
    }

    @Override
    public Long setnx(String arg0, String arg1) {

        return null;
    }

    @Override
    public Long setrange(String arg0, long arg1, String arg2) {

        return null;
    }

    @Override
    public Boolean sismember(String arg0, String arg1) {

        return null;
    }

    @Override
    public Set<String> smembers(String arg0) {

        return null;
    }

    @Override
    public List<String> sort(String arg0) {

        return null;
    }

    @Override
    public List<String> sort(String arg0, SortingParams arg1) {

        return null;
    }

    @Override
    public String spop(String arg0) {

        return null;
    }

    @Override
    public Set<String> spop(String arg0, long arg1) {

        return null;
    }

    @Override
    public String srandmember(String arg0) {

        return null;
    }

    @Override
    public List<String> srandmember(String arg0, int arg1) {

        return null;
    }

    @Override
    public Long srem(String arg0, String... arg1) {

        return null;
    }

    @Override
    public ScanResult<String> sscan(String arg0, int arg1) {

        return null;
    }

    @Override
    public ScanResult<String> sscan(String arg0, String arg1) {

        return null;
    }

    @Override
    public ScanResult<String> sscan(String arg0, String arg1, ScanParams arg2) {

        return null;
    }

    @Override
    public Long strlen(String arg0) {

        return null;
    }

    @Override
    public String substr(String arg0, int arg1, int arg2) {

        return null;
    }

    @Override
    public Long ttl(String arg0) {

        return null;
    }

    @Override
    public String type(String arg0) {

        return null;
    }

    @Override
    public Long zadd(String arg0, Map<String, Double> arg1) {

        return null;
    }

    @Override
    public Long zadd(String arg0, double arg1, String arg2) {

        return null;
    }

    @Override
    public Long zadd(String arg0, Map<String, Double> arg1, ZAddParams arg2) {

        return null;
    }

    @Override
    public Long zadd(String arg0, double arg1, String arg2, ZAddParams arg3) {

        return null;
    }

    @Override
    public Long zcard(String arg0) {

        return null;
    }

    @Override
    public Long zcount(String arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Long zcount(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Double zincrby(String arg0, double arg1, String arg2) {

        return null;
    }

    @Override
    public Double zincrby(String arg0, double arg1, String arg2, ZIncrByParams arg3) {

        return null;
    }

    @Override
    public Long zlexcount(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<String> zrange(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Set<String> zrangeByLex(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<String> zrangeByLex(String arg0, String arg1, String arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<String> zrangeByScore(String arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<String> zrangeByScore(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<String> zrangeByScore(String arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<String> zrangeByScore(String arg0, String arg1, String arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, String arg1, String arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long zrank(String arg0, String arg1) {

        return null;
    }

    @Override
    public Long zrem(String arg0, String... arg1) {

        return null;
    }

    @Override
    public Long zremrangeByLex(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Long zremrangeByRank(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long zremrangeByScore(String arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Long zremrangeByScore(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<String> zrevrange(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Set<String> zrevrangeByLex(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<String> zrevrangeByLex(String arg0, String arg1, String arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, String arg1, String arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, String arg1, String arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, String arg1, String arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long zrevrank(String arg0, String arg1) {

        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(String arg0, int arg1) {

        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(String arg0, String arg1) {

        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(String arg0, String arg1, ScanParams arg2) {

        return null;
    }

    @Override
    public Double zscore(String arg0, String arg1) {

        return null;
    }

    @Override
    public Long append(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public Long bitcount(byte[] arg0) {

        return null;
    }

    @Override
    public Long bitcount(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public List<byte[]> bitfield(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public List<byte[]> blpop(byte[] arg0) {

        return null;
    }

    @Override
    public List<byte[]> brpop(byte[] arg0) {

        return null;
    }

    @Override
    public Long decr(byte[] arg0) {

        return null;
    }

    @Override
    public Long decrBy(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public Long del(byte[] arg0) {

        return null;
    }

    @Override
    public byte[] echo(byte[] arg0) {

        return null;
    }

    @Override
    public Boolean exists(byte[] arg0) {

        return null;
    }

    @Override
    public Long expire(byte[] arg0, int arg1) {

        return null;
    }

    @Override
    public Long expireAt(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public Long geoadd(byte[] arg0, Map<byte[], GeoCoordinate> arg1) {

        return null;
    }

    @Override
    public Long geoadd(byte[] arg0, double arg1, double arg2, byte[] arg3) {

        return null;
    }

    @Override
    public Double geodist(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Double geodist(byte[] arg0, byte[] arg1, byte[] arg2, GeoUnit arg3) {

        return null;
    }

    @Override
    public List<byte[]> geohash(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public List<GeoCoordinate> geopos(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] arg0, double arg1, double arg2, double arg3, GeoUnit arg4) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] arg0, double arg1, double arg2, double arg3, GeoUnit arg4,
            GeoRadiusParam arg5) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] arg0, byte[] arg1, double arg2, GeoUnit arg3) {

        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] arg0, byte[] arg1, double arg2, GeoUnit arg3,
            GeoRadiusParam arg4) {

        return null;
    }

    @Override
    public byte[] get(byte[] arg0) {

        return null;
    }

    @Override
    public byte[] getSet(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public Boolean getbit(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public byte[] getrange(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long hdel(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public Boolean hexists(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public byte[] hget(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] arg0) {

        return null;
    }

    @Override
    public Long hincrBy(byte[] arg0, byte[] arg1, long arg2) {

        return null;
    }

    @Override
    public Double hincrByFloat(byte[] arg0, byte[] arg1, double arg2) {

        return null;
    }

    @Override
    public Set<byte[]> hkeys(byte[] arg0) {

        return null;
    }

    @Override
    public Long hlen(byte[] arg0) {

        return null;
    }

    @Override
    public List<byte[]> hmget(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public String hmset(byte[] arg0, Map<byte[], byte[]> arg1) {

        return null;
    }

    @Override
    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] arg0, byte[] arg1, ScanParams arg2) {

        return null;
    }

    @Override
    public Long hset(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Long hsetnx(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Collection<byte[]> hvals(byte[] arg0) {

        return null;
    }

    @Override
    public Long incr(byte[] arg0) {

        return null;
    }

    @Override
    public Long incrBy(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public Double incrByFloat(byte[] arg0, double arg1) {

        return null;
    }

    @Override
    public byte[] lindex(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public Long linsert(byte[] arg0, LIST_POSITION arg1, byte[] arg2, byte[] arg3) {

        return null;
    }

    @Override
    public Long llen(byte[] arg0) {

        return null;
    }

    @Override
    public byte[] lpop(byte[] arg0) {

        return null;
    }

    @Override
    public Long lpush(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public Long lpushx(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public List<byte[]> lrange(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long lrem(byte[] arg0, long arg1, byte[] arg2) {

        return null;
    }

    @Override
    public String lset(byte[] arg0, long arg1, byte[] arg2) {

        return null;
    }

    @Override
    public String ltrim(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long move(byte[] arg0, int arg1) {

        return null;
    }

    @Override
    public Long persist(byte[] arg0) {

        return null;
    }

    @Override
    public Long pexpire(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public Long pexpireAt(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public Long pfadd(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public long pfcount(byte[] arg0) {

        return 0;
    }

    @Override
    public byte[] rpop(byte[] arg0) {

        return null;
    }

    @Override
    public Long rpush(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public Long rpushx(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public Long sadd(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public Long scard(byte[] arg0) {

        return null;
    }

    @Override
    public String set(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public String set(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public String set(byte[] arg0, byte[] arg1, byte[] arg2, byte[] arg3, long arg4) {

        return null;
    }

    @Override
    public Boolean setbit(byte[] arg0, long arg1, boolean arg2) {

        return null;
    }

    @Override
    public Boolean setbit(byte[] arg0, long arg1, byte[] arg2) {

        return null;
    }

    @Override
    public String setex(byte[] arg0, int arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Long setnx(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public Long setrange(byte[] arg0, long arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Boolean sismember(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public Set<byte[]> smembers(byte[] arg0) {

        return null;
    }

    @Override
    public List<byte[]> sort(byte[] arg0) {

        return null;
    }

    @Override
    public List<byte[]> sort(byte[] arg0, SortingParams arg1) {

        return null;
    }

    @Override
    public byte[] spop(byte[] arg0) {

        return null;
    }

    @Override
    public Set<byte[]> spop(byte[] arg0, long arg1) {

        return null;
    }

    @Override
    public byte[] srandmember(byte[] arg0) {

        return null;
    }

    @Override
    public List<byte[]> srandmember(byte[] arg0, int arg1) {

        return null;
    }

    @Override
    public Long srem(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] arg0, byte[] arg1, ScanParams arg2) {

        return null;
    }

    @Override
    public Long strlen(byte[] arg0) {

        return null;
    }

    @Override
    public byte[] substr(byte[] arg0, int arg1, int arg2) {

        return null;
    }

    @Override
    public Long ttl(byte[] arg0) {

        return null;
    }

    @Override
    public String type(byte[] arg0) {

        return null;
    }

    @Override
    public Long zadd(byte[] arg0, Map<byte[], Double> arg1) {

        return null;
    }

    @Override
    public Long zadd(byte[] arg0, double arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Long zadd(byte[] arg0, Map<byte[], Double> arg1, ZAddParams arg2) {

        return null;
    }

    @Override
    public Long zadd(byte[] arg0, double arg1, byte[] arg2, ZAddParams arg3) {

        return null;
    }

    @Override
    public Long zcard(byte[] arg0) {

        return null;
    }

    @Override
    public Long zcount(byte[] arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Long zcount(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Double zincrby(byte[] arg0, double arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Double zincrby(byte[] arg0, double arg1, byte[] arg2, ZIncrByParams arg3) {

        return null;
    }

    @Override
    public Long zlexcount(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrange(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long zrank(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public Long zrem(byte[] arg0, byte[]... arg1) {

        return null;
    }

    @Override
    public Long zremrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Long zremrangeByRank(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long zremrangeByScore(byte[] arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Long zremrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrevrange(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, double arg1, double arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {

        return null;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] arg0, long arg1, long arg2) {

        return null;
    }

    @Override
    public Long zrevrank(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] arg0, byte[] arg1) {

        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] arg0, byte[] arg1, ScanParams arg2) {

        return null;
    }

    @Override
    public Double zscore(byte[] arg0, byte[] arg1) {

        return null;
    }

}
