package com.centsuse.api_auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的限流工具类
 */
@Component
public class RateLimiter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 限流检查
     * @param key 限流键（如：ip:接口路径）
     * @param limit 单位时间内的最大请求次数
     * @param expire 过期时间（秒）
     * @return 是否允许请求
     */
    public boolean tryAcquire(String key, int limit, int expire) {
        if (!StringUtils.hasText(key)) {
            return true;
        }

        try {
            // 生成Redis键
            String redisKey = "rate_limit:" + key;

            // 获取当前计数
            Long current = redisTemplate.opsForValue().increment(redisKey);

            if (current == 1) {
                // 第一次请求，设置过期时间
                redisTemplate.expire(redisKey, Duration.ofSeconds(expire));
            }

            // 检查是否超过限制
            return current <= limit;
        } catch (Exception e) {
            // Redis异常时默认允许请求，避免影响正常业务
            return true;
        }
    }

    /**
     * 基于IP的限流
     * @param ip IP地址
     * @param limit 单位时间内的最大请求次数
     * @param expire 过期时间（秒）
     * @return 是否允许请求
     */
    public boolean tryAcquireByIp(String ip, int limit, int expire) {
        return tryAcquire("ip:" + ip, limit, expire);
    }

    /**
     * 基于用户名的限流
     * @param username 用户名
     * @param limit 单位时间内的最大请求次数
     * @param expire 过期时间（秒）
     * @return 是否允许请求
     */
    public boolean tryAcquireByUsername(String username, int limit, int expire) {
        return tryAcquire("username:" + username, limit, expire);
    }

    /**
     * 基于接口路径的限流
     * @param path 接口路径
     * @param limit 单位时间内的最大请求次数
     * @param expire 过期时间（秒）
     * @return 是否允许请求
     */
    public boolean tryAcquireByPath(String path, int limit, int expire) {
        return tryAcquire("path:" + path, limit, expire);
    }

    /**
     * 基于IP和接口路径的组合限流
     * @param ip IP地址
     * @param path 接口路径
     * @param limit 单位时间内的最大请求次数
     * @param expire 过期时间（秒）
     * @return 是否允许请求
     */
    public boolean tryAcquireByIpAndPath(String ip, String path, int limit, int expire) {
        return tryAcquire("ip_path:" + ip + ":" + path, limit, expire);
    }
}
