package com.back.moyeomoyeo.repository.friend;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
                .where(memberEq(loginMember))
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    public Boolean isRequest(String nickname, Member member) {
        FriendApprove fa = queryFactory
                .selectFrom(friendApprove)
                .where(requestNicknameEq(nickname),
                        memberEq(member),
                        friendApprove.isApprove.eq(FriendApproveEnum.REQUEST)
                        , friendApprove.isProcess.eq(FriendProcessEnum.WAIT))
                .fetchFirst();

        return fa != null;
    }

    public FriendApprove findFriendApprove(String nickname, Member requestMember) {
        return queryFactory
                .selectFrom(friendApprove)
                .where(requestNicknameEq(nickname),
                        memberEq(requestMember))
                .fetchFirst();
    }


    public Boolean isDuplicateFriend(String friendNickname, Member member) {
        Friend findFriend = queryFactory
                .selectFrom(friend)
                .where(friend.friendNickname.eq(friendNickname),
                        friend.member.eq(member))
                .fetchFirst();
        return findFriend != null;
    }

    public Boolean isProcessFriend(String requestFriendNickname, Member member) {
        FriendApprove findFriendApprove = queryFactory
                .selectFrom(friendApprove)
                .where(requestNicknameEq(requestFriendNickname)
                        , memberEq(member),
                        friendApprove.isProcess.eq(FriendProcessEnum.WAIT),
                        friendApprove.isApprove.eq(FriendApproveEnum.REQUEST))
                .fetchFirst();
        return findFriendApprove != null;
    }


    public BooleanExpression memberEq(Member member) {
        return member != null ? friendApprove.member.eq(member) : null;
    }

    public BooleanExpression requestNicknameEq(String requestFriendNickname) {
        return requestFriendNickname != null ? friendApprove.requestNickname.eq(requestFriendNickname) : null;
    }


}
