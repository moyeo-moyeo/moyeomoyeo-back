package com.back.moyeomoyeo.service.tempNumber;

import com.back.moyeomoyeo.dto.tempNumber.response.SavedTempNumberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class TempNumberService {
    private final RedisTemplate<String, String> redisTemplate;
    private final int passwordLength = 8;

    public SavedTempNumberResponse savedTempNumber(String reqUser) {

        String temporaryNumber = createTemporaryNumber();
        redisTemplate.opsForValue().set(reqUser, temporaryNumber);
        redisTemplate.expire(reqUser, 3, TimeUnit.MINUTES);
        return new SavedTempNumberResponse(reqUser,temporaryNumber);
    }

    public String createTemporaryNumber() {
        SecureRandom random = new SecureRandom();
        final String passwordList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(passwordList.length());
            sb.append(passwordList.charAt(randomIndex));
        }
        return sb.toString();
    }

    public Boolean matchesTempNumber(String reqUser, String reqTempNumber ){
        if(redisTemplate.hasKey(reqUser)){
            String keyTempNumber = redisTemplate.opsForValue().get(reqUser);
            return keyTempNumber.equals(reqTempNumber);
        }
        return false;
    }

}