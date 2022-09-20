package com.back.moyeomoyeo.service.promise;

import com.back.moyeomoyeo.dto.friend.request.NewFriendReqProcessRequest;
import com.back.moyeomoyeo.dto.promise.reqeust.PromiseGeneratedRequest;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.friend.FriendErrorException;
import com.back.moyeomoyeo.repository.friend.FriendRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepositoryCustom;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.repository.promise.PromisePlaceRepository;
import com.back.moyeomoyeo.repository.promise.PromiseRepository;
import com.back.moyeomoyeo.security.AuthorizedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PromiseServiceTest {

    @Autowired
    private PromiseService promiseService;

    @Autowired
    private PromisePlaceRepository promisePlaceRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PromiseRepository promiseRepository;

    @Autowired
    private FriendRepositoryCustom friendRepositoryCustom;

    @Autowired
    private FriendRepository friendRepository;


    @BeforeEach
    private void setUp() {
        Member member1 = memberRepository.save(new Member("test1", "1234", "hi", "아으닉네임1", "19981015", "01012341234"));
        Member member2 = memberRepository.save(new Member("test2", "1234", "hi", "아으닉네임2", "19981015", "01012341234"));
        Member member3 = memberRepository.save(new Member("test3", "1234", "hi", "아으닉네임3", "19981015", "01012341234"));
        Member member4 = memberRepository.save(new Member("test4", "1234", "hi", "아으닉네임4", "19981015", "01012341234"));

        // 데이터베이스에 친구를 미리 설정
        friendRepository.save(new NewFriendReqProcessRequest().toEntity(member1, member2.getNickname()));
        friendRepository.save(new NewFriendReqProcessRequest().toEntity(member2, member1.getNickname()));

        friendRepository.save(new NewFriendReqProcessRequest().toEntity(member1, member3.getNickname()));
        friendRepository.save(new NewFriendReqProcessRequest().toEntity(member3, member1.getNickname()));

    }

    @Test
    @Transactional
    void 친구와_약속잡을때_해당친구가_친구추가되지않은_유저일경우_FriendErrorException_발생() {

        //given
        Member member = new Member("test1", "1234", "hi", "아으닉네임1", "19981015", "01012341234");
        AuthorizedUser loginMember = new AuthorizedUser(member);
        List<String> friendNicknameList = new ArrayList<>();
        friendNicknameList.add("아으닉네임2");
        friendNicknameList.add("아으닉네임3");
        friendNicknameList.add("아으닉네임4");

        //when && then
        FriendErrorException responseErrorBody = assertThrows(FriendErrorException.class, () ->
                promiseService.promiseGenerated(loginMember, new PromiseGeneratedRequest("서울역", 35, 35, "2022년 1월 20일", friendNicknameList)));


        assertThat(responseErrorBody.getFriendErrorCode().getMessage()).isEqualTo("친구 추가 안된 유저입니다.");
        assertThat(responseErrorBody.getFriendErrorCode().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}