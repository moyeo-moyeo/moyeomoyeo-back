package com.back.moyeomoyeo.service.tempNumber;

import com.back.moyeomoyeo.dto.tempNumber.response.SavedTempNumberResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@Rollback(value = false)
class TempNumberServiceTest {

    @Autowired
    TempNumberService tempNumberService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Test
    void savedTempNumber() {
        String reqUser = "test";
       SavedTempNumberResponse response =tempNumberService.savedTempNumber(reqUser);

       assertThat(redisTemplate.opsForValue().get(reqUser)).isNotNull();
       assertThat(redisTemplate.opsForValue().get(reqUser)).isEqualTo(response.getTempNumber());
    }

    @Test
    void createTemporaryNumber() {
    }


}