package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private static final long BEGIN_STAMP = 1640995200L;

    private static final int COUNT_BITS = 32;

    public long nextId(String keyPrefix) {
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_STAMP;

        // 2.生成自增序列号
        Long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" +
                now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd")));

        // 3.拼接并返回long：位运算+或运算
        return timestamp << COUNT_BITS | count;
    }

}
