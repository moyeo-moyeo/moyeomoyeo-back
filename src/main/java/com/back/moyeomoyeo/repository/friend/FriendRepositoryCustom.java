package com.back.moyeomoyeo.repository.friend;

import com.back.moyeomoyeo.dto.friend.response.FriendListResponse;
import com.back.moyeomoyeo.dto.friend.response.NewFriendIsRequestResponse;
import com.back.moyeomoyeo.dto.friend.response.QNewFriendIsRequestResponse;
import com.back.moyeomoyeo.entity.friend.Friend;
import com.back.moyeomoyeo.entity.friend.FriendApprove;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendProcessEnum;
import com.back.moyeomoyeo.entity.member.Member;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.back.moyeomoyeo.entity.friend.QFriend.friend;
import static com.back.moyeomoyeo.entity.friend.QFriendApprove.friendApprove;
import static com.back.moyeomoyeo.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    public Page<NewFriendIsRequestResponse> showNewFriendRequest(Member loginMember, Pageable pageable) {

        QueryResults<NewFriendIsRequestResponse> result = queryFactory
                .select(new QNewFriendIsRequestResponse(friendApprove.requestNickname
                        , friendApprove.isApprove,
                        friendApprove.isProcess))
                .from(friendApprove)
                .join(friendApprove.member, member)
                .where(requestNicknameEq(loginMember.getNickname()))
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    public Boolean isRequest(String friendNickname, Member loginMember) {
        FriendApprove fa = queryFactory
                .selectFrom(friendApprove)
                .where(requestNicknameEq(friendNickname).and(friendApprove.member.eq(loginMember)),
                        friendApprove.isApprove.eq(FriendApproveEnum.REQUEST)
                        , friendApprove.isProcess.eq(FriendProcessEnum.WAIT))
                .fetchFirst();

        return fa != null;
    }


    public FriendApprove findFriendApprove(Member requestMember) {
        return queryFactory
                .selectFrom(friendApprove)
                .where(requestNicknameEq(requestMember.getNickname()))
                .fetchFirst();
    }


    public Boolean isDuplicateFriend(String friendNickname, Member requestSendMemberNickname) {
        Friend findFriend = queryFactory
                .selectFrom(friend)
                .where(friend.requestGetMember.nickname.eq(friendNickname)
                        .and(friend.requestSendMemberNickname.eq(requestSendMemberNickname.getNickname())
                        ))
                .fetchFirst();
        return findFriend != null;
    }

    public Slice<FriendListResponse> friends(Pageable pageable, Member member) {
        QueryResults<Friend> result = queryFactory
                .selectFrom(friend)
                .where(friend.requestGetMember.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetchResults();


        List<FriendListResponse> content = new ArrayList<>();
        for (Friend friends : result.getResults()) {
            content.add(new FriendListResponse(friends.getId(), friends.getRequestSendMemberNickname()));
        }
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    public boolean isExistsFriend(Member loginMember, String friendNickname) {
        return queryFactory
                .select(friend)
                .from(friend)
                .where(friend.requestGetMember.eq(loginMember),
                        friend.requestSendMemberNickname.eq(friendNickname))
                .fetchFirst() != null;


    }


    public BooleanExpression memberEq(Member member) {
        return member != null ? friendApprove.member.eq(member) : null;
    }

    public BooleanExpression requestNicknameEq(String requestFriendNickname) {
        return requestFriendNickname != null ? friendApprove.requestNickname.eq(requestFriendNickname) : null;
    }


}
