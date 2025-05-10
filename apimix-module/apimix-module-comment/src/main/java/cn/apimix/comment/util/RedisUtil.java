package cn.apimix.comment.util;

/**
 * @Author: Hor
 * @Date: 2025/2/1 12:18
 * @Version: 1.0
 */

import cn.apimix.core.utils.ConvertUtils;
import org.redisson.api.*;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Redis工具类
 * java内置的4大核心函数接口
 *
 * 消费型接口 Consumer<T>        void accept(T t)
 * 供给型接口 Supplier<T>        T get()
 * 函数型即可 Function<T, R>     R apply(T t)
 * 断定型接口 Predicate<T>       boolean test(T t)
 */
@Component
public class RedisUtil {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 执行事务操作
     * @param consumer 事务处理函数，使用RTransaction执行操作
     */
    public void execute(Consumer<RedisUtil> consumer) {
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());
        try {
            consumer.accept(this);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Transaction failed", e);
        }
    }

    /**
     * 设置键的过期时间
     * @param key 键
     * @param time 时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key, long time, TimeUnit unit) {
        return redissonClient.getBucket(key).expire(time, unit);
    }

    public boolean expireOfSeconds(String key, long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    public boolean expireOfHours(String key, long time) {
        return expire(key, time, TimeUnit.HOURS);
    }

    /**
     * 获取键的剩余过期时间（秒）
     * @param key 键
     * @return 剩余时间（秒）
     */
    public long getExpire(String key) {
        return redissonClient.getBucket(key).remainTimeToLive() / 1000;
    }

    /**
     * 检查键是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 删除键
     * @param keys 键数组
     * @return 删除的键数量
     */
    public long delete(String... keys) {
        return redissonClient.getKeys().delete(keys);
    }

    public long delete(Collection<String> keys) {
        return redissonClient.getKeys().delete(keys.toArray(new String[0]));
    }

    /**
     * 模糊删除键
     * @param pattern 匹配模式
     */
    public void deleteByPattern(String pattern) {
        redissonClient.getKeys().deleteByPattern(pattern);
    }

    /**
     * 扫描匹配的键
     * @param pattern 匹配模式
     * @return 匹配的键列表
     */
    public List<String> scan(String pattern) {
        List<String> keys = new ArrayList<>();
        RKeys rKeys = redissonClient.getKeys();
        Iterable<String> iterable = rKeys.getKeysByPattern(pattern);
        iterable.forEach(keys::add);
        return keys;
    }

    // ============================= String =============================
    public <T> T get(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key, new JsonJacksonCodec());
        return bucket.get();
    }


    public void set(String key, Object value) {
        redissonClient.getBucket(key).set(value);
    }

    public void set(String key, Object value, long time, TimeUnit unit) {
        redissonClient.getBucket(key).set(value, time, unit);
    }

    public Long increment(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(delta);
    }

    /**
     * 递增 1
     *
     * @param key 键
     * @return 当前值
     * @since 2.0.1
     */
    public long incr(String key) {
        return redissonClient.getAtomicLong(key).incrementAndGet();
    }

    /**
     * 递减 1
     *
     * @param key 键
     * @return 当前值
     * @since 2.0.1
     */
    public long decr(String key) {
        return redissonClient.getAtomicLong(key).decrementAndGet();
    }

    public Long decrement(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(-delta);
    }

    /**
     * 检查 Set 集合中是否存在指定值
     * (Redisson 实现版)
     *
     * @param key   键
     * @param value 要检查的值
     * @return true-存在 / false-不存在 或 key 不存在
     */
    public Boolean sHasKey(String key, Object value) {
        // 1. 获取 Redisson 的 Set 对象
        // 说明: 通过 redissonClient 获取分布式 Set 对象，泛型为 Object 以兼容不同数据类型
        RSet<Object> rSet = redissonClient.getSet(key,new JsonJacksonCodec());

        // 2. 检查值是否存在
        // 原理: Redisson 的 contains 方法对应 Redis 的 SISMEMBER 命令
        // 注意: 如果 key 不存在，会返回 false（与 Redis 原生行为一致）
        // 3. 返回检查结果
        return rSet.contains(value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, Object... values) {
        RSet<Object> rSet = redissonClient.getSet(key,new JsonJacksonCodec());
        return (long) rSet.addAllCounted(Arrays.asList(values));
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key       键
     * @return
     */
    public Set sGet(String key) {
        RSet<Object> rSet = redissonClient.getSet(key,new JsonJacksonCodec());
        return rSet.readAll();
    }

    public <T> Set<T> sGet(String key, Class<T> clazz) {
        return new LinkedHashSet<>(ConvertUtils.cloneDeep(sGet(key), clazz));
    }


    // ============================= Hash =============================
    public <HK, HV> HV hGet(String key, HK hashKey) {
        RMap<HK, HV> map = redissonClient.getMap(key);
        return map.get(hashKey);
    }

    public <HK, HV> Map<HK, HV> hGetAll(String key) {
        RMap<HK, HV> map = redissonClient.getMap(key);
        return map.readAllMap();
    }

    public <HK> boolean hExists(String key, HK hashKey) {
        return redissonClient.getMap(key).containsKey(hashKey);
    }

    public <HK, HV> void hSet(String key, HK hashKey, HV value) {
        redissonClient.getMap(key).put(hashKey, value);
    }

    public <HK, HV> void hMSet(String key, Map<HK, HV> map) {
        redissonClient.getMap(key).putAll(map);
    }

    public <HK> long hDelete(String key, HK... hashKeys) {
        return redissonClient.getMap(key).fastRemove(Arrays.asList(hashKeys));
    }

    public <HK> Long hIncrBy(String key, HK hashKey, long delta) {
        return (Long) redissonClient.getMap(key).addAndGet(hashKey, delta);
    }

    // ============================= Set =============================
    public <V> Set<V> sMembers(String key) {
        RSet<V> set = redissonClient.getSet(key);
        return set.readAll();
    }

    public <V> boolean sIsMember(String key, V member) {
        return redissonClient.getSet(key).contains(member);
    }

    public <V> long sAdd(String key, V... members) {
        return redissonClient.getSet(key).addAll(Arrays.asList(members)) ? members.length : 0;
    }

    public <V> long sRemove(String key, V... members) {
        return redissonClient.getSet(key,new JsonJacksonCodec()).removeAll(Arrays.asList(members)) ? members.length : 0;
    }

    // ============================= List =============================
    public <V> List<V> lRange(String key, int start, int end) {
        return (List<V>) redissonClient.getList(key).range(start, end);
    }



    public <V> long lLen(String key) {
        return redissonClient.getList(key).size();
    }

    // ============================= ZSet =============================
    public <V> boolean zAdd(String key, V member, double score) {
        return redissonClient.getScoredSortedSet(key).add(score, member);
    }

    public <V> Set<V> zRange(String key, int start, int end) {
        return (Set<V>) redissonClient.getScoredSortedSet(key).valueRange(start, end);
    }

    public <V> Double zScore(String key, V member) {
        return redissonClient.getScoredSortedSet(key).getScore(member);
    }

    public <V> Integer zRank(String key, V member) {
        return redissonClient.getScoredSortedSet(key).rank(member);
    }

    public <V> Long zRemove(String key, V... members) {
        return redissonClient.getScoredSortedSet(key).removeAll(Arrays.asList(members)) ? members.length : 0L;
    }

    // ============================= 限流 =============================
    public boolean rateLimit(String key, RateType type, int rate, int rateInterval) {
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        return limiter.trySetRate(type, rate, rateInterval, RateIntervalUnit.SECONDS) &&
                limiter.tryAcquire(1);
    }
}
