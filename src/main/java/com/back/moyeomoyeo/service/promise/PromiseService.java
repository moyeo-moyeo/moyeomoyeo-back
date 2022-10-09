package com.back.moyeomoyeo.service.promise;

import com.back.moyeomoyeo.dto.promise.reqeust.PromiseGeneratedRequest;
import com.back.moyeomoyeo.dto.promise.response.PromisePlaceResponse;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.entity.promise.Promise;
import com.back.moyeomoyeo.entity.promise.PromisePlace;
import com.back.moyeomoyeo.errorhandle.friend.FriendErrorCode;
import com.back.moyeomoyeo.errorhandle.friend.FriendErrorException;
import com.back.moyeomoyeo.repository.friend.FriendRepository;
import com.back.moyeomoyeo.repository.friend.FriendRepositoryCustom;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.repository.promise.PromisePlaceRepository;
import com.back.moyeomoyeo.repository.promise.PromiseRepository;
import com.back.moyeomoyeo.security.AuthorizedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromiseService {
    private final PromisePlaceRepository promisePlaceRepository;
    private final MemberRepository memberRepository;

    private final PromiseRepository promiseRepository;


    private final FriendRepositoryCustom friendRepositoryCustom;

    private final FriendRepository friendRepository;

    @Transactional
    public PromisePlaceResponse promiseGenerated(AuthorizedUser authorizedUser, PromiseGeneratedRequest promiseGeneratedRequest) {

        Member findMember = memberRepository.findByLoginId(authorizedUser.getUsername());
        List<String> friendNicknameList = promiseGeneratedRequest.getFriendNicknameList();
        PromisePlace promisePlace = promiseGeneratedRequest.toEntity();


        List<String> members = new ArrayList<>();
        promisePlaceRepository.save(promisePlace);
        promiseRepository.save(new Promise(promisePlace, findMember));
        members.add(findMember.getNickname());
        for (int i = 0; i < friendNicknameList.size(); i++) {
            if (!friendRepositoryCustom.isExistsFriend(findMember, memberRepository.findByNickname(friendNicknameList.get(i)))) {
                throw new FriendErrorException(FriendErrorCode.NOT_FRIEND_EXISTS);
            }
            members.add(friendNicknameList.get(i));
            Member findFriendNickname = memberRepository.findByNickname(friendNicknameList.get(i));
            promiseRepository.save(new Promise(promisePlace, findFriendNickname));
        }

        return new PromisePlaceResponse(promisePlace.getPlaceName(), members, promiseGeneratedRequest.getMeetingDate());
    }

}
