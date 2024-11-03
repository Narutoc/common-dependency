package com.local.naruto.common.utils;

import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.BadRequestException;
import com.local.naruto.common.exception.DataCheckException;
import com.local.naruto.common.exception.ServiceException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

/**
 * redis工具类
 *
 * @author naruto chen
 * @since 2023-09-26
 */
@Component
public class RedisUtils<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 获取key自增的字符串，自增过期时间为1天
     *
     * @param key     关键字
     * @param timeout 过期时间，单位天
     * @param length  规定取得位数
     * @return 关键字key的自增信息
     */
    public String getIncrementWithExpire(String key, Long timeout, int length) {
        long number = getRedisIncrementNum(key, timeout);
        return getExistNumberString(number, length);
    }

    /**
     * 生成不过期的自增编号
     *
     * @param key    关键字
     * @param length 长度
     * @return 自增值
     */
    public String getIncrementWithoutExpire(String key, int length) {
        RedisAtomicLong entityIdCounter =
            new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long counter = entityIdCounter.addAndGet(1L);
        return getExistNumberString(counter, length);
    }

    /**
     * 从redis中获取key对应的自增数值
     *
     * @param key 缓存Key
     * @return 数字
     */
    private long getRedisIncrementNum(String key, Long timeout) {
        // 获取key对应的原子数值
        RedisAtomicLong idCounter =
            new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long counter = idCounter.incrementAndGet();
        // 自增重新开始，设置过期时间
        if (counter == 1) {
            idCounter.expire(Objects.isNull(timeout) ? 1 : timeout, TimeUnit.DAYS);
        }
        return counter;
    }

    /**
     * 获取数值指定长度对应额字符串，不足以0补充
     *
     * @param existNumber    现有的原始数值
     * @param requiredLength 要求返回的字符串位数
     * @return seq对应length的字符串
     */
    private String getExistNumberString(long existNumber, int requiredLength) {
        String value = String.valueOf(existNumber);
        int valueLength = value.length();
        // 现有数值对应的字符串长度大于等于指定的长度，则直接返回现有数值对应的字符串
        if (valueLength >= requiredLength) {
            return value;
        }
        // 现有数值对应的字符串长度大于小于指定的长度，则用0代替
        int rest = requiredLength - valueLength;
        return "0".repeat(rest) + value;
    }

    /**
     * 设施key的失效时间
     *
     * @param key  键
     * @param time 时间（秒）
     */
    public void setExpireTime(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception exception) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());

        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键不能为null
     * @return 时间（秒）
     */
    public long getExpireTime(String key) {
        if (!hasKey(key)) {
            throw new DataCheckException(Constants.INT_400, String.format("%s不存在", key));
        }
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断Key是否存在
     *
     * @param key 键
     * @return true存在，false不存在
     */
    public boolean hasKey(String key) {
        if (Objects.isNull(key)) {
            throw new BadRequestException(Constants.INT_400, "redis key 为空");
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个或者多个
     */
    public void deleteKey(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
                return;
            }
            redisTemplate.delete(Arrays.asList(key));
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern key
     */
    public Set<String> deleteKeysByPattern(String pattern) {
        if (StringUtils.isNotEmpty(pattern)) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (CollectionUtils.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
                return keys;
            }
        }
        return null;
    }

    /**
     * 根据key获取value
     *
     * @param key 键
     * @return 值
     */
    public T get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public void setKeyAndValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒）
     * @return true 成功， false 失败
     */
    public boolean setKeyAndValueWithExpireTime(String key, T value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setKeyAndValue(key, value);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key    键
     * @param offset 递增因子（大于0）
     * @return 结果
     */
    public long incrementKey(String key, long offset) {
        if (offset < 0) {
            throw new BadRequestException(Constants.INT_400, "递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, offset);
    }

    /**
     * 递减
     *
     * @param key    键
     * @param offset 递减因子（大于0）
     * @return 结果
     */
    public long decrement(String key, long offset) {
        if (offset < 0) {
            throw new BadRequestException(Constants.INT_400, "递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -offset);
    }

    /**
     * hash递减
     *
     * @param key    键
     * @param item   项
     * @param offset 要减少记(小于0)
     * @return 结果
     */
    public double hashDecrement(String key, String item, double offset) {
        return redisTemplate.opsForHash().increment(key, item, -offset);
    }

    /**
     * 模糊查询获取key值
     *
     * @param pattern 模糊值
     * @return 集合
     */
    public Set<String> getRedisKeysByPattern(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 使用Redis的消息队列
     *
     * @param channel 通道
     * @param message 消息内容
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
