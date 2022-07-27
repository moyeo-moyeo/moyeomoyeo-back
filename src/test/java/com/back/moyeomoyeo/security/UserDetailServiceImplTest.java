package com.back.moyeomoyeo.security;

import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc //need this in Spring Boot test
class UserDetailServiceImplTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Member testUser = new Member("testId", "testPw",
                "이름", "닉네임", "990319", "010-4183-2288");

        testUser.encodingPassword(passwordEncoder.encode(testUser.getPassword()));
        memberRepository.save(testUser);

    }

    @Test
    @DisplayName("로그인_요청_성공")
    @Transactional
    void 로그인_요청_성공() throws Exception {
        mockMvc.perform(formLogin()
                .user("testId")
                .password("testPw")).andExpect(authenticated());
      }

    @Test
    @DisplayName("로그인_요청_실패")
    @Transactional
    void 로그인_요청_실패() throws Exception {
        mockMvc.perform(formLogin()
                .user("testId")
                .password("testPd")).andExpect(unauthenticated());
    }
}