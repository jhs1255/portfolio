package com.berry_comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int LENGTH = 13;
    public Object getData(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    public void setData(String key, Object value) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    public void setData(String key, Object value, long timeout) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Duration expire = Duration.ofSeconds(timeout);
        operations.set(key, value, expire);
    }
    public void delData(String key) {
        redisTemplate.delete(key);
    }

    public String setPassword(String key) {
        String password = createTempPass();
        //10분동안 유효한 키값
        setData(key,password,600);
        return password;
    }

    public String createTempPass(){
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
