package com.back.moyeomoyeo.repository.member;

import com.back.moyeomoyeo.entity.member.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.back.moyeomoyeo.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public boolean existsMemberLoginIdOrNickname(String loginId, String nickname) {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(loginIdEq(loginId).or(nicknameEq(nickname)))
                .fetchFirst();
        return findMember != null;
    }

    public boolean existsLoginId(String loginId) {

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.loginId.eq(loginId))
                .fetchFirst();

        return findMember != null;
    }

    public boolean existsNickname(String nickname) {

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.nickname.eq(nickname))
                .fetchFirst();

        return findMember != null;
    }

    public Member findByNickname(String nickname) {
        return queryFactory
                .selectFrom(member)
                .where(nicknameEq(nickname))
                .fetchOne();
    }

    public Member findByUsername(String username) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(username))
                .fetchFirst();
    }


    private BooleanExpression loginIdEq(String loginId) {
        return loginId != null ? member.loginId.eq(loginId) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return username != null ? member.username.eq(username) : null;
    }

    private BooleanExpression nicknameEq(String nickname) {
        return nickname != null ? member.nickname.eq(nickname) : null;
    }
}
