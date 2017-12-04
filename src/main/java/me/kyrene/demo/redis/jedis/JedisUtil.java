package me.kyrene.demo.redis.jedis;

import redis.clients.jedis.*;
import redis.clients.jedis.params.Params;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by wanglin on 2017/11/29.
 */
public class JedisUtil {
    private static JedisPool jedisPool = null;

    private static final JedisUtil jedisUtil = new JedisUtil();

    private JedisUtil() {

    }

    static {
        Properties properties = PropertyUtil.loadProperties("redis.properties");
        String host = properties.getProperty("redis.host");
        String port = properties.getProperty("redis.port");
        String pass = properties.getProperty("redis.pass");
        String timeout = properties.getProperty("redis.timeout");
        String maxIdle = properties.getProperty("redis.maxIdle");
        String maxTotal = properties.getProperty("redis.maxTotal");
        String maxWaitMillis = properties.getProperty("redis.maxWaitMillis");
        String testOnBorrow = properties.getProperty("redis.testOnBorrow");

        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(Integer.parseInt(maxTotal));
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(Integer.parseInt(maxIdle));
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(Boolean.valueOf(testOnBorrow));

        jedisPool = new JedisPool(config, host, Integer.parseInt(port), Integer.parseInt(timeout), pass);
    }

    /**
     * 获取jedis
     *
     * @return
     */
    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static JedisUtil getInstance() {
        return jedisUtil;
    }

    /**
     * 归还jedis
     *
     * @param jedis
     */
    private void returnResource(Jedis jedis) {
        if (null != jedis && null != jedisPool) {
            jedis.close();
        }
    }

    /**
     * 关闭jedis
     *
     * @param jedis
     */
    private void close(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
    //-----------------------------------string---------------------------

    /**
     * 设置String类型 键值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 返回 key 的值，如果 key 不存在时，返回 null。 如果 key 不是字符串类型，那么返回一个错误。
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 截取得到的子字符串。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String getRange(String key, int start, int end) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.getrange(key, (long) start, (long) end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null 。
     * 当 key 存在但不是字符串类型时，返回一个错误。
     *
     * @param key
     * @param newValue
     * @return
     */
    public String getSet(String key, String newValue) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.getSet(key, newValue);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 当偏移量 OFFSET 比字符串值的长度大，或者 key 不存在时，返回 fasle （不是很明白）
     *
     * @param key
     * @param offset
     * @return
     */
    public Boolean getBit(String key, int offset) {
        Jedis jedis = null;
        Boolean result = false;
        try {
            jedis = getJedis();
            result = jedis.getbit(key, offset);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令返回所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 null
     *
     * @param keys
     * @return
     */
    public List<String> mGet(String... keys) {
        Jedis jedis = null;
        List<String> values = null;
        try {
            jedis = getJedis();
            values = jedis.mget(keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return values;
    }

    /**
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。(不是很理解)
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public Boolean setBit(String key, int offset, Boolean value) {
        Jedis jedis = null;
        Boolean result = false;
        try {
            jedis = getJedis();
            result = jedis.setbit(key, offset, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public String setEx(String key, int seconds, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 指定的 key 不存在时，为 key 设置指定的值。 返回1 成功，返回0 不成功
     *
     * @param key
     * @param value
     * @return
     */
    public Long setNx(String key, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始。
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public Long setRange(String key, int offset, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.setrange(key, (long) offset, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取指定 key 所储存的字符串值的长度。当 key 储存的不是字符串值时，返回一个错误。
     *
     * @param key
     * @return
     */
    public Long strLen(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.strlen(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于同时设置一个或多个 key-value 对。
     *
     * @param keysValues
     * @return
     */
    public String mSet(String... keysValues) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.mset(keysValues);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于所有给定 key 都不存在时，同时设置一个或多个 key-value 对。
     * 当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0
     *
     * @param keysValues
     * @return
     */
    public Long mSetNx(String... keysValues) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.msetnx(keysValues);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令以毫秒为单位设置 key 的生存时间。 返回ok 代表设置成功
     *
     * @param key
     * @param milliseconds
     * @param value
     * @return
     */
    public String PSetEx(String key, long milliseconds, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.psetex(key, milliseconds, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * Redis Incr 命令将 key 中储存的数字值增一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @return 增1 后的值
     */
    public Long incr(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.incr(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令将 key 中储存的数字加上指定的增量值。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @param amount
     * @return 返回增加一定数值后的值
     */
    public Long incrBy(String key, long amount) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.incrBy(key, amount);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令为 key 中所储存的值加上指定的浮点数增量值。
     * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作。
     *
     * @param key
     * @param amount
     * @return
     */
    public Double incrByFloat(String key, double amount) {
        Jedis jedis = null;
        Double result = null;
        try {
            jedis = getJedis();
            result = jedis.incrByFloat(key, amount);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令将 key 中储存的数字值减一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @return 减1 后的值
     */
    public Long decr(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.decr(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令将 key 所储存的值减去指定的减量值。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @param amount
     * @return 返回减一定数值后的值
     */
    public Long decrBy(String key, long amount) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.decrBy(key, amount);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * Append 命令用于为指定的 key 追加值。
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     *
     * @param key
     * @param value
     * @return 增加后的字符串的长度
     */
    public Long append(String key, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.append(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于删除已存在的键。不存在的 key 会被忽略。
     *
     * @param key
     * @return 删除成功返回1
     */
    public Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于序列化给定 key ，并返回被序列化的值。
     * 如果 key 不存在，那么返回 null
     *
     * @param key
     * @return
     */
    public byte[] dump(String key) {
        Jedis jedis = null;
        byte[] result = null;
        try {
            jedis = getJedis();
            result = jedis.dump(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于检查给定 key 是否存在。
     *
     * @param key
     * @return key 存在返回 true ，否则返回 false 。
     */
    public Boolean exists(String key) {
        Jedis jedis = null;
        Boolean result = null;
        try {
            jedis = getJedis();
            result = jedis.exists(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于设置 key 的过期时间。key 过期后将不再可用。 以秒为单位
     *
     * @param key
     * @param seconds
     * @return 返回1表示成功
     */
    public Long exPire(String key, int seconds) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于以 UNIX 时间戳(unix timestamp)格式设置 key 的过期时间。key 过期后将不再可用。 以秒为单位
     *
     * @param key
     * @param unixTime
     * @return 返回1表示成功
     */
    public Long exPireAt(String key, Long unixTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.expireAt(key, unixTime);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于设置 key 的过期时间。key 过期后将不再可用。 以毫秒为单位
     *
     * @param key
     * @param milliSeconds
     * @return 返回1表示成功
     */
    public Long pExPire(String key, Long milliSeconds) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.pexpire(key, milliSeconds);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于以 UNIX 时间戳(unix timestamp)格式设置 key 的过期时间。key 过期后将不再可用。 以毫秒为单位
     *
     * @param key
     * @param milliunixTime
     * @return 返回1表示成功
     */
    public Long pExPireAt(String key, Long milliunixTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.pexpireAt(key, milliunixTime);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于查找所有符合给定模式 pattern 的 key
     *
     * @param pattern
     * @return 符合给定模式的 key 列表
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.keys(pattern);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 选择数据库  默认是0
     *
     * @param index
     * @return 成功返回ok
     */
    public String select(int index) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.select(index);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于将当前数据库的 key 移动到给定的数据库 db 当中。
     *
     * @param key
     * @param index
     * @return 返回1 表示移动成功
     */
    public Long move(String key, int index) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.move(key, index);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于移除给定 key 的过期时间，使得 key 永不过期
     *
     * @param key
     * @return 当过期时间移除成功时，返回 1 。 如果 key 不存在或 key 没有设置过期时间，返回 0 \
     */
    public Long persist(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.persist(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 以毫秒为单位返回 key 的剩余过期时间。
     *
     * @param key
     * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以毫秒为单位，返回 key 的剩余生存时间。
     */
    public Long pttl(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.pttl(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令以秒为单位返回 key 的剩余过期时间。
     *
     * @param key
     * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
     */
    public Long ttl(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.pttl(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令从当前数据库中随机返回一个 key 。
     *
     * @return 当数据库不为空时，返回一个 key 。 当数据库为空时，返回 null 。
     */
    public String randomKey() {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.randomKey();
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于修改 key 的名称 。
     *
     * @param oldKeyName
     * @param newKeyName
     * @return 改名成功时提示 OK ，失败时候返回一个错误。
     * 当 OLD_KEY_NAME 和 NEW_KEY_NAME 相同，或者 OLD_KEY_NAME 不存在时，返回一个错误。 当 NEW_KEY_NAME 已经存在时， RENAME 命令将覆盖旧值。
     */
    public String rename(String oldKeyName, String newKeyName) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.rename(oldKeyName, newKeyName);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于在新的 key 不存在时修改 key 的名称 。
     *
     * @param oldKeyName
     * @param newKeyName
     * @return 修改成功时，返回 1 。 如果 NEW_KEY_NAME 已经存在，返回 0 。
     */
    public Long renameNx(String oldKeyName, String newKeyName) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.renamenx(oldKeyName, newKeyName);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 返回 key 所储存的值的类型。
     *
     * @param key
     * @return none (key不存在)
     * string (字符串)
     * list (列表)
     * set (集合)
     * zset (有序集)
     * hash (哈希表)
     */
    public String type(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.type(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    //--------------------------------------hash-------------------------------------

    /**
     * 命令用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。
     *
     * @param key
     * @param fields
     * @return 被成功删除字段的数量，不包括被忽略的字段。
     */
    public Long hDel(String key, String... fields) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.hdel(key, fields);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 查看哈希表的指定字段是否存在。
     *
     * @param key
     * @param field
     * @return 如果哈希表含有给定字段，返回 1 。 如果哈希表不含有给定字段，或 key 不存在，返回 0 。
     */
    public Boolean hExists(String key, String field) {
        Jedis jedis = null;
        Boolean result = null;
        try {
            jedis = getJedis();
            result = jedis.hexists(key, field);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 令用于返回哈希表中指定字段的值。
     *
     * @param key
     * @param field
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null 。
     */
    public String hGet(String key, String field) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.hget(key, field);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 令用于返回哈希表中，所有的字段和值。
     *
     * @param key
     * @return key-value
     */
    public Map<String, String> hGetAll(String key) {
        Jedis jedis = null;
        Map<String, String> result = null;
        try {
            jedis = getJedis();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于为哈希表中的字段值加上指定增量值。
     * 增量也可以为负数，相当于对指定字段进行减法操作。
     * 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
     * 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。
     * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @param field
     * @param number
     * @return 哈希表中字段的值。
     */
    public Long hIncrBy(String key, String field, long number) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.hincrBy(key, field, number);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于为哈希表中的字段值加上指定浮点数增量值。
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
     *
     * @param key
     * @param field
     * @param number
     * @return 哈希表中字段的值。
     */
    public Double hIncrByFloat(String key, String field, double number) {
        Jedis jedis = null;
        Double result = null;
        try {
            jedis = getJedis();
            result = jedis.hincrByFloat(key, field, number);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }


    /**
     * 用于获取哈希表中的所有域（field）。
     *
     * @param key
     * @return
     */
    public Set<String> hKeys(String key) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.hkeys(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于获取哈希表中字段的数量。
     *
     * @param key
     * @return
     */
    public Long hLen(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.hlen(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于返回哈希表中，一个或多个给定字段的值。
     * 如果指定的字段不存在于哈希表，那么返回一个 null 值。
     *
     * @param key
     * @param fields
     * @return
     */
    public List<String> hMget(String key, String... fields) {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.hmget(key, fields);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于同时将多个 field-value (字段-值)对设置到哈希表中。
     * 此命令会覆盖哈希表中已存在的字段。
     * 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。
     *
     * @param key
     * @param map
     * @return 返回ok表示成功
     */
    public String hMset(String key, Map<String, String> map) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.hmset(key, map);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于为哈希表中的字段赋值 。
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果字段已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key
     * @param field
     * @param value
     * @return 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
     */
    public Long hSet(String key, String field, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.hset(key, field, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于为哈希表中不存在的的字段赋值 。
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果字段已经存在于哈希表中，操作无效。
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     *
     * @param key
     * @param field
     * @param value
     * @return 设置成功，返回 1 。 如果给定字段已经存在且没有操作被执行，返回 0 。
     */
    public Long hSetNx(String key, String field, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.hsetnx(key, field, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 令返回哈希表所有域(field)的值。
     *
     * @param key
     * @return 一个包含哈希表中所有域(field)值的列表。 当 key 不存在时，返回一个空表。
     */
    public List<String> hVals(String key) {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.hvals(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    //--------------------------------------------List------------------------------------------------

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * @param keys
     * @param timeOut
     * @return 如果列表为空，返回一个 nil 。
     */
    public List<String> bLpop(int timeOut, String... keys) {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.blpop(timeOut, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param timeOut
     * @param keys
     * @return 如果列表为空，返回一个 nil 。
     */
    public List<String> bRpop(int timeOut, String... keys) {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.brpop(timeOut, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param list1
     * @param list2
     * @param timeOut
     * @return 有返回值，没有返回null
     */
    public String bRpoplPush(String list1, String list2, int timeOut) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.brpoplpush(list1, list2, timeOut);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param index
     * @return 标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 null 。
     */
    public String lIndex(String key, long index) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.lindex(key, index);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于在列表的元素前（输入2）或者后(输入1)插入元素。 当指定元素不存在于列表中时，不执行任何操作。 当列表不存在时，被视为空列表，不执行任何操作。 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @param existsValue
     * @param newValue
     * @param num
     * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到指定元素 ，返回 -1 。 如果 key 不存在或为空列表，返回 0 。
     */
    public Long lInsert(String key, String existsValue, String newValue, int num) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            if (num == 1) {
                result = jedis.linsert(key, BinaryClient.LIST_POSITION.AFTER, existsValue, newValue);
            } else if (num == 2) {
                result = jedis.linsert(key, BinaryClient.LIST_POSITION.BEFORE, existsValue, newValue);
            } else {
                throw new IllegalArgumentException("please enter true number");
            }

        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 令用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @return 列表的长度。
     */
    public Long lLen(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.llen(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于移除并返回列表的第一个元素。
     *
     * @param key
     * @return 列表的第一个元素。 当列表 key 不存在时，返回 null 。
     */
    public String lPop(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.lpop(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param values
     * @return 列表的长度。
     */
    public Long lPush(String key, String... values) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.lpush(key, values);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 将一个值插入到已存在的列表头部，列表不存在时操作无效。
     *
     * @param key
     * @param values
     * @return 列表的长度。
     */
    public Long lPushx(String key, String... values) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.lpushx(key, values);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return 包含指定区间内的元素。
     */
    public List<String> lRange(String key, long start, long end) {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.lrange(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。
     * COUNT 的值可以是以下几种：
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     *
     * @param key
     * @param count
     * @param value
     * @return 被移除元素的数量。 列表不存在时返回 0 。
     */
    public Long lRem(String key, long count, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.lrem(key, count, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 通过索引来设置元素的值。
     * 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。
     *
     * @param key
     * @param index
     * @param value
     * @return 操作成功返回 ok ，否则返回错误信息。
     */
    public String lSet(String key, long index, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.lset(key, index, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return 命令执行成功时，返回 ok 。
     */
    public String lTrim(String key, long start, long end) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.ltrim(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于移除并返回列表的最后一个元素。
     *
     * @param key
     * @return 列表的最后一个元素。 当列表不存在时，返回 null 。
     */
    public String rPop(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.rpop(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
     *
     * @param list1
     * @param list2
     * @return 被弹出的元素。
     */
    public String rPoplPush(String list1, String list2) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.rpoplpush(list1, list2);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于将一个或多个值插入到列表的尾部(最右边)。
     * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param values
     * @return 列表的长度。
     */
    public Long rPush(String key, String... values) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.rpush(key, values);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 用于将一个值插入到已存在的列表尾部(最右边)。如果列表不存在，操作无效。
     *
     * @param key
     * @param values
     * @return 列表的长度。
     */
    public Long rPushx(String key, String... values) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.rpushx(key, values);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    //--------------------------set-------------------

    /**
     * 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。
     * 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。
     * 当集合 key 不是集合类型时，返回一个错误。
     * 注意：在Redis2.4版本以前， SADD 只接受单个成员值。
     *
     * @param key
     * @param values
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
     */
    public Long sAdd(String key, String... values) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.sadd(key, values);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令返回集合中元素的数量。
     *
     * @param key
     * @return 集合的数量。 当集合 key 不存在时，返回 0 。
     */
    public Long sCard(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.scard(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令返回给定集合之间的差集。不存在的集合 key 将视为空集。
     * 差集的结果来着前面的 FIRST_KEY ,而不是后面的 OTHER_KEY1，也不是整个 FIRST_KEY OTHER_KEY1..OTHER_KEYN 的差集。
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * SDIFF key1 key2 key3 = {b,d}
     *
     * @param keys
     * @return 包含差集成员的列表
     */
    public Set<String> sDiff(String... keys) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.sdiff(keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令将给定集合之间的差集存储在指定的集合中。如果指定的集合 key 已存在，则会被覆盖。
     *
     * @param dstkey
     * @param keys
     * @return 结果集中的元素数量。
     */
    public Long sDiffStore(String dstkey, String... keys) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.sdiffstore(dstkey, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令返回给定所有给定集合的交集。 不存在的集合 key 被视为空集。 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
     *
     * @param keys
     * @return 交集成员的列表
     */
    public Set<String> sInter(String... keys) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.sinter(keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令将给定集合之间的交集存储在指定的集合中。如果指定的集合已经存在，则将其覆盖。
     *
     * @param dstkey
     * @param keys
     * @return 交集成员的列表
     */
    public Long sInteStore(String dstkey, String... keys) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.sinterstore(dstkey, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
     *
     * @param key
     * @return 集合中的所有成员
     */
    public Set<String> sMembers(String key) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.smembers(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令将指定成员 member 元素从 source 集合移动到 destination 集合。
     * SMOVE 是原子性操作。
     * 如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。
     * 当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
     * 当 source 或 destination 不是集合类型时，返回一个错误。
     *
     * @param source
     * @param destination
     * @param member
     * @return 如果成员元素被成功移除，返回 1 。 如果成员元素不是 source 集合的成员，并且没有任何操作对 destination 集合执行，那么返回 0 。
     */
    public Long sMove(String source, String destination, String member) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.smove(source, destination, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于移除并返回集合中的一个随机元素。
     *
     * @param key
     * @return 被移除的随机元素。 当集合不存在或是空集时，返回 null
     */
    public String sPop(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.spop(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于返回集合中的一个随机元素。
     * 从 Redis 2.6 版本开始， Srandmember 命令接受可选的 count 参数：
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。
     * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
     * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 Srandmember 则仅仅返回随机元素，而不对集合进行任何改动。
     *
     * @param key
     * @param count
     * @return 只提供集合 key 参数时，返回一个元素；如果集合为空，返回 null 。 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组。
     */
    public List<String> sRandMember(String key, int count) {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.srandmember(key, count);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。
     * 当 key 不是集合类型，返回一个错误。
     *
     * @param key
     * @param members
     * @return 被成功移除的元素的数量，不包括被忽略的元素
     */
    public Long sRem(String key, String... members) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.srem(key, members);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /***
     * 命令返回给定集合的并集。不存在的集合 key 被视为空集。
     * @param keys
     * @return 并集成员的列表。
     */
    public Set<String> sUnion(String... keys) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.sunion(keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令将给定集合的并集存储在指定的集合 destination 中。如果 destination 已经存在，则将其覆盖
     *
     * @param destkey
     * @param keys
     * @return 结果集中的元素数量。
     */
    public Long sUnionStore(String destkey, String... keys) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.sunionstore(destkey, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    //-------------------------------------sorted set----------------------------

    /**
     * 令用于将一个或多个成员元素及其分数值加入到有序集当中。
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。
     * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zAdd(String key, double score, String member) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zadd(key, score, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Long zAdd(String key, Map<String, Double> map) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zadd(key, map);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于计算集合中元素的数量。
     *
     * @param key
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 null。
     */
    public Long zCard(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zcard(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于计算有序集合中指定分数区间的成员数量。
     *
     * @param key
     * @param min
     * @param max
     * @return 分数值在 min（包含） 和 max(包含) 之间的成员的数量。
     */
    public Long zCount(String key, double min, double max) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zcount(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令计算给定的一个或多个有序集的交集，其中给定 key 的数量必须以 numkeys 参数指定，并将该交集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。
     *
     * @param destkey
     * @param keys
     * @return 保存到目标结果集的的成员数量。
     */
    public Long zInterStore(String destkey, String... keys) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zinterstore(destkey, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Long zInterStore(String destkey, ZParams zParams, String... keys) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zinterstore(destkey, zParams, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 成员名称前需要加 [ 符号作为开头, [ 符号与成员之间不能有空格
     * 可以使用 - 和 + 表示得分最小值和最大值
     * min 和 max 不能反, max 放前面 min放后面会导致返回结果为0
     * 计算成员之间的成员数量时,参数 min 和 max 的位置也计算在内。
     * min 和 max 参数的含义与 zrangebylex 命令中所描述的相同
     * redis> ZLEXCOUNT myzset - +
     * (integer) 7
     * redis> ZLEXCOUNT myzset [c +
     * (integer) 5
     * redis> ZLEXCOUNT myzset - [c
     * (integer) 3
     *
     * @param key
     * @param min
     * @param max
     * @return 指定区间内的成员数量。
     */
    public Long zLexCount(String key, String min, String max) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zlexcount(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }


    /**
     * 返回有序集中，指定区间内的成员。
     * 其中成员的位置按分数值递增(从小到大)来排序。
     * 具有相同分数值的成员按字典序(lexicographical order )来排列。
     *
     * @param key
     * @param start
     * @param end
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    public Set<String> zRange(String key, long start, long end) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.zrange(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 通过字典区间返回有序集合的成员。
     * - + 最小到最大
     * [ 包含 ( 不包含
     *
     * @param key
     * @param min
     * @param max
     * @return 指定区间内的元素列表。
     */
    public Set<String> zRangeByLex(String key, String min, String max) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.zrangeByLex(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。
     * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。
     * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
     *
     * @param key
     * @param min
     * @param max
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    public Set<String> zRangeByScore(String key, String min, String max) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Set<String> zRangeByScore(String key, Double min, Double max) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
     *
     * @param key
     * @param memeber
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。 如果成员不是有序集 key 的成员，返回 null 。
     * 实例
     */
    public Long zRank(String key, String memeber) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zrank(key, memeber);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 令用于移除有序集中的一个或多个成员，不存在的成员将被忽略。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param memebers
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public Long zRem(String key, String... memebers) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zrem(key, memebers);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 令用于移除有序集合中给定的字典区间的所有成员。
     *
     * @param key
     * @param min
     * @param max
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public Long zRemRangeRyLex(String key, String min, String max) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zremrangeByLex(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于移除有序集中，指定排名(rank)区间内的所有成员。
     *
     * @param key
     * @param start
     * @param end
     * @return 被移除成员的数量。
     */
    public Long zRemRangeRyRank(String key, long start, long end) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令用于移除有序集中，指定分数（score）区间内的所有成员。
     *
     * @param key
     * @param min
     * @param max
     * @return 被移除成员的数量。
     */
    public Long zRemRangeRyScore(String key, Double min, Double max) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Long zRemRangeRyScore(String key, String min, String max) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令返回有序集中，指定区间内的成员。
     *  其中成员的位置按分数值递减(从大到小)来排列。
     *  具有相同分数值的成员按字典序的逆序(reverse lexicographical order)排列。
     * @param key
     * @param start
     * @param end
     * @return  指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    public Set<String> zRevRange(String key, long start, long end) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = getJedis();
            result = jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null 。
     * @param key
     * @param member
     * @return 返回score
     */
    public Double zScore(String key, String member) {
        Jedis jedis = null;
        Double result = null;
        try {
            jedis = getJedis();
            result = jedis.zscore(key,member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 命令计算给定的一个或多个有序集的并集，其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。
     * @param dstkey
     * @param keys
     * @return 保存到 destination 的结果集的成员数量。
     */
    public Long zUnionStore(String dstkey, String... keys) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zunionstore(dstkey,keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }
    /**
     * 命令计算给定的一个或多个有序集的并集，其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。
     * @param dstkey
     * @param keys
     * @return 保存到 destination 的结果集的成员数量。
     */
    public Long zUnionStore(String dstkey,ZParams params ,String... keys) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = getJedis();
            result = jedis.zunionstore(dstkey,params,keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return result;
    }

}
