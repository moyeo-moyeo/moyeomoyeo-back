package com.back.moyeomoyeo.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDetailServiceImplTest {


    @Test
    @DisplayName("로그인_요청_성공")
    void 로그인_요청_성공() {
        /*TODO
         *  - mvc를 통한 로그인 테스트 해야함*/
    }
}