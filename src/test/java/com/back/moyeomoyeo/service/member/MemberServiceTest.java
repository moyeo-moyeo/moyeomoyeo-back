package com.back.moyeomoyeo.service.member;

import com.back.moyeomoyeo.dto.member.request.MemberRequest;
import com.back.moyeomoyeo.dto.member.response.MemberResponse;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorCode;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.repository.member.MemberRepositoryCustom;
import com.back.moyeomoyeo.security.AuthorizedUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    @Spy
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    MemberRepositoryCustom memberRepositoryCustom;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    MemberRequest newMember() {
        return new MemberRequest("test", "1234", "1234", "테스터", "아으닉넥임",
                "981015", "01012341234");
    }

    @Test
    @DisplayName("유저의 정보를 입력하면 회원가입 성공 메시지와 해당 유저의 id값을 반환합니다.")
    void newUser() {
        // given
        MemberRequest memberRequest = newMember();
        when(memberRepository.save(any())).thenReturn(memberRequest.toEntity());

        //when
        MemberResponse memberResponse = memberService.newUser(memberRequest);
        //then
        verify(memberService, atLeastOnce()).newUser(memberRequest);

        assertThat(memberResponse.getId()).isEqualTo(memberRequest.toEntity().getId());
        assertThat(memberResponse.getMessage()).isEqualTo("회원가입 성공");
    }

    @Test
    @DisplayName("이미 가입된 닉네임이 있을 경우 ErrorException 예외가 발생합니다.")
    void duplicateNickname() {
        //given
        Member member = newMember().toEntity();
        given(memberRepositoryCustom.existsNickname(any())).willReturn(true);


        //when && then
        assertThrows(ErrorException.class, () ->
                memberService.isNickname(member.getNickname()));
        verify(memberService, atLeastOnce()).isNickname(member.getNickname());
    }

    @Test
    @DisplayName("이미 가입된 아이디가 있을경우 ErrorException 예외가 발생합니다.")
    void duplicateLoginId() {
        // given
        Member member = newMember().toEntity();
        given(memberRepositoryCustom.existsLoginId(any())).willReturn(true);

        //when && then
        assertThrows(ErrorException.class, () ->
                memberService.isLoginId(member.getLoginId()));
        verify(memberService, atLeastOnce()).isLoginId(member.getLoginId());
    }

    @Test
    @DisplayName("비밀번호와 비밀번호 재확인이 일치하지 않을 경우 ErrorException 예외가 발생합니다.")
    void passwordNotEqualsConfirmPassword() {

        //given
        MemberRequest memberRequest = new MemberRequest("test", "1234!", "1234", "테스터", "아으닉넥임",
                "981015", "01012341234");

        lenient().doThrow(new ErrorException(ErrorCode.DUPLICATE_LOGINID_OR_NICKNAME)).when(memberService).newUser(memberRequest);

        // when && then
        assertThrows(ErrorException.class, () -> memberService.newUser(memberRequest));


    }


    @Test
    @DisplayName("임시 비밀번호 생성")
    void create_temporary_password() throws NoSuchAlgorithmException {
        String temporaryPassword = memberService.createTemporaryPassword();
        String temporaryPassword2 = memberService.createTemporaryPassword();

        assertThat(temporaryPassword).isNotEmpty();
        assertThat(temporaryPassword).isNotEqualTo("");
        assertThat(temporaryPassword).isInstanceOf(String.class);
        assertThat(temporaryPassword.length()).isLessThan(9);
        assertThat(temporaryPassword2).isNotEqualTo(temporaryPassword);
    }

    @Test
    @DisplayName("이전 비밀번호 동일여부 확인")
    void is_authorized_password() {
        BCryptPasswordEncoder bCryptPasswordEncoder1 = new BCryptPasswordEncoder();
        String testPassword = "test";

        Member member = new Member("test", bCryptPasswordEncoder1.encode(testPassword), "username",
                "nickname", "1999-03-19", "010-4183-2288");

        doReturn(new AuthorizedUser(member)).when(memberService).sessionUser();

        assertThat(memberService.isAuthorizedPassword(testPassword)).isEqualTo(Boolean.TRUE);


    }

}

