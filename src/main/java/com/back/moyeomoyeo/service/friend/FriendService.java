package com.back.moyeomoyeo.service.friend;

import com.back.moyeomoyeo.dto.friend.request.NewFriendReqProcessRequest;
import com.back.moyeomoyeo.dto.friend.request.NewFriendRequest;
import com.back.moyeomoyeo.dto.friend.response.FriendListResponse;
import com.back.moyeomoyeo.dto.friend.response.NewFriendIsRequestResponse;
import com.back.moyeomoyeo.dto.friend.response.NewFriendResponse;
import com.back.moyeomoyeo.entity.friend.FriendApprove;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendProcessEnum;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorCode;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.friend.FriendApproveRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepositoryCustom;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.repository.member.MemberRepositoryCustom;
import com.back.moyeomoyeo.security.AuthorizedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendApproveRepository friendApproveRepository;

    private final FriendRepository friendRepository;

    private final FriendRepositoryCustom friendRepositoryCustom;
    private final MemberRepository memberRepository;

    private final MemberRepositoryCustom memberRepositoryCustom;


    @Transactional
    public NewFriendResponse newFriendRequest(AuthorizedUser loginMember, NewFriendRequest newFriendRequest) {

        Member findMember = memberRepository.findByLoginId(loginMember.getUsername());

        if (!memberRepositoryCustom.existsNickname(newFriendRequest.getFriendNickname())) {
            throw new ErrorException(ErrorCode.NOT_EXISTS_USER);
        }
        if (newFriendRequest.getFriendNickname().equals(findMember.getNickname())) {
            throw new ErrorException(ErrorCode.NOT_ADD_MYSELF);
        }

        Member getMember = memberRepositoryCustom.findByNickname(newFriendRequest.getFriendNickname());

        if (friendRepositoryCustom.isRequest(getMember.getNickname(), findMember)) {
            throw new ErrorException(ErrorCode.DUPLICATE_ADD_REQUEST_FRIEND);
        }
        if (friendRepositoryCustom.isDuplicateFriend(findMember.getNickname(), getMember)) {
            throw new ErrorException(ErrorCode.DUPLICATE_FRIEND);
        }


        FriendApprove friendApprove = newFriendRequest.toEntity(getMember, findMember.getNickname());
        friendApproveRepository.save(friendApprove);

        return new NewFriendResponse("친구 요청 완료");
    }

    @Transactional
    public Page<NewFriendIsRequestResponse> getNewFriendRequest(AuthorizedUser authorizedUser, Pageable pageable) {
        Member loginMember = memberRepository.findByLoginId(authorizedUser.getUsername());
        return friendRepositoryCustom.showNewFriendRequest(loginMember, pageable);
    }


    @Transactional
    public NewFriendResponse newFriendRequestProcess(AuthorizedUser loginMember, NewFriendReqProcessRequest newFriendReqProcessRequest) {
        Member member = memberRepository.findByLoginId(loginMember.getUsername());
        String message = "";
        FriendApproveEnum isApprove;
        FriendApprove friendApprove = friendRepositoryCustom.findFriendApprove(newFriendReqProcessRequest.getFriendNickname(), member);
        if (newFriendReqProcessRequest.getIsApprove() == FriendApproveEnum.REFUSE) {
            message = "친구 요청을 거절하였습니다.";
            isApprove = FriendApproveEnum.REFUSE;
        } else {
            message = "친구 요청을 수락하였습니다.";
            isApprove = FriendApproveEnum.AGREE;
            friendRepository.save(newFriendReqProcessRequest.toEntity(member, newFriendReqProcessRequest.getFriendNickname()));
        }
        friendApprove.processFriendStatus(isApprove, FriendProcessEnum.PROCESS);


        return new NewFriendResponse(message);
    }

    public Slice<FriendListResponse> friends(Pageable pageable) {
        AuthorizedUser member = sessionUser();

        return friendRepositoryCustom.friends(pageable, member.getMember());
    }


    protected AuthorizedUser sessionUser() {
        return (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
