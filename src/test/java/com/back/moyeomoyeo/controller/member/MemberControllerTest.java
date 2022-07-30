package com.back.moyeomoyeo.controller.member;

import com.back.moyeomoyeo.service.member.MemberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MemberControllerTest {

    private MockMvc mockMvc;

    @Mock
    MemberService memberService;
    @BeforeAll
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MemberController(memberService)).build();
    }

    @Test
    private void reqUpdatePassword(){

    }

    public class  TddMemberController {

    }
}

