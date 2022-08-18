package com.back.moyeomoyeo.service.member;

import com.back.moyeomoyeo.dto.member.request.MemberRequest;
import com.back.moyeomoyeo.dto.member.request.MemberUpdatePasswordRequest;
import com.back.moyeomoyeo.dto.member.response.MemberIssueTempNumberResponse;
import com.back.moyeomoyeo.dto.member.response.MemberResponse;
import com.back.moyeomoyeo.dto.member.response.MemberUpdatePasswordResponse;
import com.back.moyeomoyeo.dto.tempNumber.response.SavedTempNumberResponse;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorCode;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.repository.member.MemberRepositoryCustom;
import com.back.moyeomoyeo.security.AuthorizedUser;
import com.back.moyeomoyeo.service.tempNumber.TempNumberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    TempNumberService tempNumberService;

    @Spy
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String testPassword = "test";

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
    @DisplayName("입력한 닉네임이 가입한 닉네임이 아닐경우 \"사용 가능한 닉네임입니다.\"를 반환합니다.")
    void disposableNickname() {

        //given
        doReturn(new MemberDuplicateResponse("사용 가능한 닉네임입니다.")).when(memberService).isNickname(anyString());

        // when
        MemberDuplicateResponse response = memberService.isNickname(anyString());

        //then
        then(memberService).should().isNickname(anyString());
        assertThat(response.getMessage()).isEqualTo("사용 가능한 닉네임입니다.");
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
    @DisplayName("입력한 아이디가 가입한 아이디가 아닐경우 \"사용 가능한 아이디입니다.\"를 반환합니다.")
    void disposableLoginId() {
        //given
        doReturn(new MemberDuplicateResponse("사용 가능한 아이디입니다.")).when(memberService).isLoginId(anyString());

        //when
        MemberDuplicateResponse response = memberService.isLoginId(anyString());

        //then
        assertThat(response.getMessage()).isEqualTo("사용 가능한 아이디입니다.");
        then(memberService).should().isLoginId(anyString());
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
    void create_temporary_password() {
        String temporaryPassword = tempNumberService.createTemporaryNumber();
        String temporaryPassword2 = tempNumberService.createTemporaryNumber();

        assertThat(temporaryPassword).isNotEmpty();
        assertThat(temporaryPassword).isNotEqualTo("");
        assertThat(temporaryPassword).isInstanceOf(String.class);
        assertThat(temporaryPassword.length()).isLessThan(9);
        assertThat(temporaryPassword2).isNotEqualTo(temporaryPassword);
    }


    @Test
    @DisplayName("이전 비밀번호 동일여부 확인")
    void is_authorized_password() {
        Member member = getMockMember();

        doReturn(new AuthorizedUser(member)).when(memberService).sessionUser();


        assertThat(memberService.isAuthorizedPassword(testPassword)).isEqualTo(Boolean.TRUE);


    }

    private Member getMockMember() {
        BCryptPasswordEncoder bCryptPasswordEncoder1 = new BCryptPasswordEncoder();
        return new Member("test", bCryptPasswordEncoder1.encode(testPassword), "username",
                "nickname", "1999-03-19", "010-4183-2288");
    }

    @Test
    @DisplayName("비밀번호 수정 매서드 테스트")
    void doUpdatePassword() {

        Member member = getMockMember();

        doReturn(new AuthorizedUser(member)).when(memberService).sessionUser();
        when(memberRepository.findByLoginId(member.getLoginId())).thenReturn(member);

        MemberUpdatePasswordResponse memberUpdatePasswordResponse = memberService.doUpdateTempPassword();

        assertThat(memberUpdatePasswordResponse.getBeforePassword()).isNotEqualTo("");
        assertThat(memberUpdatePasswordResponse.getCurrentPassword()).isNotEqualTo("");
        assertThat(memberUpdatePasswordResponse.getBeforePassword()).isNotEqualTo(memberUpdatePasswordResponse.getCurrentPassword());
    }


    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword() {
        /* 순서
         * 1 : Patch 요청으로 현재비밀번호, 바꿀 비밀번호, 비밀번호 확인
         * 2 : 현재 비밀번호가 나의 비밀번호 맞는지 확인
         * 3 : 맞으면 비밀번호 업데이트
         * 4 : 결과 전송
         *
         * 결과
         * Member.getPassword == 바꿀 비밀번호
         * */
        //given
        BCryptPasswordEncoder bCryptPasswordEncoder1 = new BCryptPasswordEncoder();

        MemberUpdatePasswordRequest memberUpdatePasswordRequest = new MemberUpdatePasswordRequest("test", "changePassword", "changePassword");
        Member member = getMockMember();

        //when\
        doReturn(new AuthorizedUser(member)).when(memberService).sessionUser();
        when(memberRepository.findByLoginId(anyString())).thenReturn(member);
        MemberUpdatePasswordResponse memberUpdatePasswordResponse =
                memberService.changePassword(memberUpdatePasswordRequest);
        //then
        assertThat(bCryptPasswordEncoder1.matches(memberUpdatePasswordRequest.getAfterPassword(), memberUpdatePasswordResponse.getCurrentPassword()))
                .isEqualTo(true);
    }


    @Test
    @DisplayName("임시번호 발급 요청")
    void reqIssueTempNumber() {
        /*
         * 1. 사용자가 이름만 요청으로  요청
         * 2. Redis에 (사용자이름,임시번호) 로 저장
         * 3. SMS 보내기
         *
         * 다른 method
         * 1. reqUser 회원이 있는지 확인 ok
         * 2. createTemporaryNumber();
         * 3. RedisTemplate -> 따로 임시번호를 위한 클래스르 빼는게 맞는거 같음.
         * 4. sms 송신
         * */
        String reqUser = "test";
        SavedTempNumberResponse savedTempNumberResponse = new SavedTempNumberResponse(reqUser,"1q2w3e4r");
        //when
        when(memberRepository.existsByLoginId(reqUser)).thenReturn(true);
        when(tempNumberService.savedTempNumber(reqUser)).thenReturn(savedTempNumberResponse);

        MemberIssueTempNumberResponse issueTempNumberResponse = memberService.reqIssueTempNumber(reqUser);

        //then
        assertThat(issueTempNumberResponse).isNotNull();
        assertThat(issueTempNumberResponse.getTempNumber().length()).isEqualTo(8);
        assertThat(issueTempNumberResponse.getMessage()).isEqualTo("임시번호가 발급되었습니다");


    }


}