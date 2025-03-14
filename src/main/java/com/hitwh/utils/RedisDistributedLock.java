package com.hitwh.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.management.ConstructorParameters;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisDistributedLock {

    private StringRedisTemplate stringRedisTemplate;

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey 锁的键名
     * @param requestId 请求标识，用于释放锁时验证
     * @param expireTime 锁的过期时间，单位：秒
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String requestId, int expireTime) {
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey 锁的键名
     * @param requestId 请求标识，用于验证是否是同一个请求释放锁
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
        if (currentValue != null && currentValue.equals(requestId)) {
            stringRedisTemplate.delete(lockKey);
            return true;
        }
        return false;
    }
}
