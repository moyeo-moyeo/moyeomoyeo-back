package com.back.moyeomoyeo.service.friend;

import com.back.moyeomoyeo.dto.friend.request.NewFriendRequest;
import com.back.moyeomoyeo.dto.friend.response.NewFriendResponse;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorCode;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.friend.FriendApproveRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepositoryCustom;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.security.AuthorizedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FriendServiceTest {


    @InjectMocks
    @Spy
    private FriendService friendService;
    @Mock
    private FriendApproveRepository friendApproveRepository;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private FriendRepositoryCustom friendRepositoryCustom;
    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    public void setMemberUp() {
        memberRepository.save(new Member("test", "1234", "hoe", "아으닉네임", "981212", "01011112222"));
        memberRepository.save(new Member("test12", "1234", "hoe12", "아으닉네임12", "981212", "01011112222"));
    }


    @Test
    void 로그인한_사용자가_입력한닉네임을_가진유저에게_친구요청_성공시_친구요청완료메시지_반환() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981212", "01011112222");
        AuthorizedUser loginMember = new AuthorizedUser(member);
        //when
        NewFriendResponse newFriendResponse = new NewFriendResponse("친구 요청 완료");
        doReturn(newFriendResponse).when(friendService).newFriendRequest(loginMember, new NewFriendRequest("아으닉네임12"));
        //then
        assertThat(newFriendResponse.getMessage()).isEqualTo("친구 요청 완료");

    }

    @Test
    void 로그인한_사용자가_입력한닉네임이_존재하지않을경우_ErrorException_발생() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981212", "01011112222");
        AuthorizedUser loginMember = new AuthorizedUser(member);

        //when
        doThrow(new ErrorException(ErrorCode.NOT_EXISTS_USER)).when(friendService).newFriendRequest(loginMember, new NewFriendRequest("없음"));
        ErrorCode response = assertThrows(ErrorException.class, () ->
                friendService.newFriendRequest(loginMember, new NewFriendRequest("없음"))).getErrorCode();

        //then
        verify(friendService, atLeastOnce()).newFriendRequest(any(), any());
        assertThat(response.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void 로그인한_사용자가_입력한닉네임이_자기자신이면_ErrorException_발생() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981212", "01011112222");
        AuthorizedUser loginMember = new AuthorizedUser(member);

        //when
        doThrow(new ErrorException(ErrorCode.NOT_ADD_MYSELF)).when(friendService).newFriendRequest(loginMember, new NewFriendRequest("아으닉네임"));
        ErrorCode responseError = assertThrows(ErrorException.class, () ->
                friendService.newFriendRequest(loginMember, new NewFriendRequest("아으닉네임"))).getErrorCode();
        //then

        verify(friendService, atLeastOnce()).newFriendRequest(any(), any());
        assertThat(responseError.getMessage()).isEqualTo("본인을 친구 추가할 수 없습니다.");
        assertThat(responseError.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

    }


}