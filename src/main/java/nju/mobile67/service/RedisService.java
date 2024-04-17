package nju.mobile67.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getLogs(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Async
    public void updateCacheAsync(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value, 3, TimeUnit.HOURS);
    }

    public void deleteLogs(String key) {
        stringRedisTemplate.delete(key);
    }

    public void cacheChatResponse(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value, 3, TimeUnit.HOURS);
    }

    public String getChatResponse(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
