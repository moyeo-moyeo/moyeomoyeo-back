package com.back.moyeomoyeo.service.friend;

import com.back.moyeomoyeo.dto.friend.request.NewFriendRequest;
import com.back.moyeomoyeo.dto.friend.response.NewFriendResponse;
import com.back.moyeomoyeo.entity.friend.Friend;
import com.back.moyeomoyeo.entity.friend.FriendApprove;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.friend.FriendApproveRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepositoryCustom;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.security.AuthorizedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class FriendServiceTest {

    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendApproveRepository friendApproveRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private FriendRepositoryCustom friendRepositoryCustom;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    void 로그인한사용자가_입력한닉네임이_존재하지않으면_ErrorException_발생() {

        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        memberRepository.save(member);
        AuthorizedUser loginMember = new AuthorizedUser(member);

        //when
        ErrorException errorResponse = assertThrows(ErrorException.class, () ->
                friendService.newFriendRequest(loginMember, new NewFriendRequest("닉네임")));

        //then
        assertThat(errorResponse.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getErrorCode().getMessage()).isEqualTo("존재하지 않는 유저입니다.");

    }

    @Test
    void 로그인한사용자가_입력한닉네임이_자기자신이면_ErrorException_발생() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        memberRepository.save(member);
        AuthorizedUser loginMember = new AuthorizedUser(member);

        //when

        ErrorException errorResponse = assertThrows(ErrorException.class, () ->
                friendService.newFriendRequest(loginMember, new NewFriendRequest("아으닉네임")));

        //then
        assertThat(errorResponse.getErrorCode().getMessage()).isEqualTo("본인을 친구 추가할 수 없습니다.");
        assertThat(errorResponse.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 로그인한사용자가_입력한닉네임이_이미_요청한닉네임이면_ErrorException_발생() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        Member requestMember = new Member("test12", "1234", "hi",
                "제2의인생", "981015", "01012341234");
        memberRepository.save(member);
        memberRepository.save(requestMember);
        AuthorizedUser loginMember = new AuthorizedUser(member);

        //when
        friendService.newFriendRequest(loginMember, new NewFriendRequest("제2의인생"));
        ErrorException errorResponse = assertThrows(ErrorException.class, () ->
                friendService.newFriendRequest(loginMember, new NewFriendRequest("제2의인생")));

        //then
        assertThat(errorResponse.getErrorCode().getMessage()).isEqualTo("이미 친구 요청을 보낸 사용자입니다.");
        assertThat(errorResponse.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void 로그인한사용자가_입력한닉네임이_이미_친구추가된_사용자일경우_ErrorException_발생() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        Member requestMember = new Member("test12", "1234", "hi",
                "제2의인생", "981015", "01012341234");
        memberRepository.save(member);
        memberRepository.save(requestMember);
        AuthorizedUser loginMember = new AuthorizedUser(member);
        friendRepository.save(new Friend(requestMember, member.getNickname()));

        //when
        ErrorException errorResponse = assertThrows(ErrorException.class, () ->
                friendService.newFriendRequest(loginMember, new NewFriendRequest(requestMember.getNickname())));

        //then
        assertThat(errorResponse.getErrorCode().getMessage()).isEqualTo("이미 친구 추가된 사용자입니다.");
        assertThat(errorResponse.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void 로그인한사용자가_입력한닉네임으로_친구요청() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        Member requestMember = new Member("test12", "1234", "hi",
                "제2의 인생", "981015", "01012341234");
        memberRepository.save(requestMember);
        memberRepository.save(member);
        AuthorizedUser loginMember = new AuthorizedUser(member);

        //when
        NewFriendResponse friendRequestResponse = friendService.newFriendRequest(loginMember, new NewFriendRequest("제2의 인생"));
        Optional<FriendApprove> requestSendMember = friendApproveRepository.findById(member.getId());
        //then
        assertThat(friendRequestResponse.getMessage()).isEqualTo("친구 요청 완료");
        assertThat(requestSendMember.orElseGet(() -> null).getIsApprove()).isEqualTo(FriendApproveEnum.REQUEST);


    }

}