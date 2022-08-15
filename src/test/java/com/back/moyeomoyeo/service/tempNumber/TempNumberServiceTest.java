package com.back.moyeomoyeo.service.tempNumber;

import com.back.moyeomoyeo.dto.tempNumber.response.SavedTempNumberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
class TempNumberServiceTest {

    @Autowired
    TempNumberService tempNumberService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("임시번호를 발급하여 Redis 3분동안 저장")
    void savedTempNumber() {
        String reqUser = "test";
        SavedTempNumberResponse response = tempNumberService.savedTempNumber(reqUser);

        assertThat(redisTemplate.opsForValue().get(reqUser)).isNotNull();
        assertThat(redisTemplate.opsForValue().get(reqUser)).isEqualTo(response.getTempNumber());
    }

    @Test
    @DisplayName("임시번호와 사용자요청 임시번호 일치")
    void matchesTempNumber() {
        String reqUser = "test";
        SavedTempNumberResponse response = tempNumberService.savedTempNumber(reqUser);


        Boolean aBoolean = tempNumberService.matchesTempNumber(reqUser, response.getTempNumber());

        assertThat(aBoolean).isEqualTo(Boolean.TRUE);
    }

    @Test
    @DisplayName("임시번호와 사용자요청 임시번호 불일치")
    void notMatchesTempNumber() {
        String reqUser = "test";
        SavedTempNumberResponse response = tempNumberService.savedTempNumber(reqUser);


        Boolean aBoolean = tempNumberService.matchesTempNumber(reqUser, "1asq23d");

        assertThat(aBoolean).isEqualTo(Boolean.FALSE);
    }


    @Test
    void createTemporaryNumber() {
    }


}