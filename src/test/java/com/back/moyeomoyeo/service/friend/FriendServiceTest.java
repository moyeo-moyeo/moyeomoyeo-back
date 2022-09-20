package com.back.moyeomoyeo.service.friend;

import com.back.moyeomoyeo.dto.friend.request.NewFriendReqProcessRequest;
import com.back.moyeomoyeo.dto.friend.request.NewFriendRequest;
import com.back.moyeomoyeo.dto.friend.response.NewFriendIsRequestResponse;
import com.back.moyeomoyeo.dto.friend.response.NewFriendResponse;
import com.back.moyeomoyeo.entity.friend.Friend;
import com.back.moyeomoyeo.entity.friend.FriendApprove;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendProcessEnum;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        NewFriendRequest 제2의인생 = new NewFriendRequest("제2의인생");
        friendService.newFriendRequest(loginMember, 제2의인생);
        ErrorException errorResponse = assertThrows(ErrorException.class, () ->
                friendService.newFriendRequest(loginMember, 제2의인생));

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
        FriendApprove friendRequest = friendApproveRepository.findByRequestNickname(member.getNickname());
        //then
        assertThat(friendRequestResponse.getMessage()).isEqualTo("친구 요청 완료");
        assertThat(friendRequest.getIsApprove()).isEqualTo(FriendApproveEnum.REQUEST);
        assertThat(friendRequest.getIsProcess()).isEqualTo(FriendProcessEnum.WAIT);

    }


    @Test
    void 친구요청_받은사용자가_친구요청을_거절하면_친구_요청을_거절하였습니다_응답() {
        //given
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        Member requestMember = new Member("test12", "1234", "hi",
                "제2의 인생", "981015", "01012341234");
        memberRepository.save(requestMember);
        memberRepository.save(member);
        AuthorizedUser requestSendLoginMember = new AuthorizedUser(member);
        friendService.newFriendRequest(requestSendLoginMember, new NewFriendRequest(requestMember.getNickname()));

        List<FriendApprove> all = friendApproveRepository.findAll();
        for (FriendApprove friendApprove : all) {
            System.out.println("friendApprove = " + friendApprove);
        }

        //when
        AuthorizedUser requestGetLoginMember = new AuthorizedUser(requestMember);
        NewFriendResponse newFriendResponse = friendService.newFriendRequestProcess(requestGetLoginMember,
                new NewFriendReqProcessRequest(member.getNickname(), FriendApproveEnum.REFUSE));
        FriendApprove friendRequest = friendApproveRepository.findByRequestNickname(requestMember.getNickname());

        //then
        assertThat(newFriendResponse.getMessage()).isEqualTo("친구 요청을 거절하였습니다.");
        assertThat(friendRequest.getIsApprove()).isEqualTo(FriendApproveEnum.REFUSE);
        assertThat(friendRequest.getIsProcess()).isEqualTo(FriendProcessEnum.PROCESS);

    }

    @Test
    void 친구요청_받은사용자가_친구요청을_거절하면_친구_요청을_수락하였습니다_응답() {
        Member member = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        Member requestMember = new Member("test12", "1234", "hi",
                "제2의 인생", "981015", "01012341234");
        memberRepository.save(requestMember);
        memberRepository.save(member);
        AuthorizedUser requestSendLoginMember = new AuthorizedUser(member);
        friendService.newFriendRequest(requestSendLoginMember, new NewFriendRequest(requestMember.getNickname()));

        //when
        AuthorizedUser requestGetLoginMember = new AuthorizedUser(requestMember);
        NewFriendResponse newFriendResponse = friendService.newFriendRequestProcess(requestGetLoginMember,
                new NewFriendReqProcessRequest(requestSendLoginMember.getMember().getNickname(), FriendApproveEnum.AGREE));
        FriendApprove friendRequest = friendApproveRepository.findByRequestNickname(member.getNickname());
        //then
        assertThat(newFriendResponse.getMessage()).isEqualTo("친구 요청을 수락하였습니다.");
        assertThat(friendRequest.getIsApprove()).isEqualTo(FriendApproveEnum.AGREE);
        assertThat(friendRequest.getIsProcess()).isEqualTo(FriendProcessEnum.PROCESS);
    }

    @Test
    void 로그인한사용자는_친구요청을_수락한_사용자들의_닉네임_리스트_응답() {
        //given
        Member member1 = new Member("test", "1234", "hoe",
                "아으닉네임", "981015", "01012341234");
        Member member2 = new Member("test12", "1234", "hi",
                "제2의 인생", "981015", "01012341234");
        Member member3 = new Member("te2", "1234", "hi",
                "친구하자", "981017", "01022222222");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        AuthorizedUser requestSendLoginMember = new AuthorizedUser(member2);
        friendService.newFriendRequest(requestSendLoginMember, new NewFriendRequest(member1.getNickname()));
        AuthorizedUser requestSendLoginMember2 = new AuthorizedUser(member3);
        friendService.newFriendRequest(requestSendLoginMember2, new NewFriendRequest(member1.getNickname()));
        AuthorizedUser requestGetLoginMember = new AuthorizedUser(member1);

        friendService.newFriendRequestProcess(requestGetLoginMember,
                new NewFriendReqProcessRequest(requestSendLoginMember.getMember().getNickname(), FriendApproveEnum.AGREE));
        friendService.newFriendRequestProcess(requestGetLoginMember,
                new NewFriendReqProcessRequest(requestSendLoginMember2.getMember().getNickname(), FriendApproveEnum.AGREE));


        //when
        PageRequest pageable = PageRequest.of(1, 2);
        Page<NewFriendIsRequestResponse> newFriendRequest = friendService.getNewFriendRequest(requestGetLoginMember, pageable);
        List<NewFriendIsRequestResponse> content = newFriendRequest.getContent();
        NewFriendIsRequestResponse newFriendIsRequestResponse1 = content.get(0);
        NewFriendIsRequestResponse newFriendIsRequestResponse2 = content.get(1);
        //then
        assertThat(content.size()).isEqualTo(2);
        assertThat(newFriendIsRequestResponse1.getRequestMember()).isEqualTo(member2.getNickname());
        assertThat(newFriendIsRequestResponse2.getRequestMember()).isEqualTo(member3.getNickname());
    }

}