package com.back.moyeomoyeo.service.tempNumber;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public class TempNumberService {
    public TempNumberService() {
    }

    public String savedTempNumber() {
        return "임시";
    }

    public String createTemporaryNumber() {
        SecureRandom random = new SecureRandom();
        final String passwordList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        StringBuilder sb = new StringBuilder();

        int passwordLength = 8;
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(passwordList.length());
            sb.append(passwordList.charAt(randomIndex));
        }
        return sb.toString();
    }
}