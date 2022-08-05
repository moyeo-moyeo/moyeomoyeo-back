package com.back.moyeomoyeo.entity.tempNumber;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class TempNumberTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Test
    @Transactional
    public void testStrings() {
        System.out.println("host = " + host);
        System.out.println("port = " + port);
        System.out.println("password = " + password);
        final String key = "key";
        redisTemplate.opsForValue().set(key, "1");

        String s = redisTemplate.opsForValue().get(key);
        System.out.println("result = " + s);
    }
}