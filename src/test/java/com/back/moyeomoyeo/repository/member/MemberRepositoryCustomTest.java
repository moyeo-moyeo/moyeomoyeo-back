package com.back.moyeomoyeo.repository.member;

import com.back.moyeomoyeo.entity.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.back.moyeomoyeo.entity.member.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryCustomTest {


    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 가입된_닉네임_또는_아이디가_없을경우_True_반환합니다() {
        //given
        String loginId = "hoe";
        String nickname = "아으닉네임";

        //when
        Member findMember = queryFactory.selectFrom(member)
                .where(member.loginId.eq(loginId).or(member.nickname.eq(nickname)))
                .fetchFirst();

        boolean isExists = findMember == null;

        //then
        assertThat(isExists).isTrue();

    }

    @Test
    void 가입된_닉네임_존재할경우_False_반환() {
        //given
        String nickname = "아으닉네임";
        memberRepository.save(
                new Member("hoe", "1234", "hello",
                        "아으닉네임", "19981015", "01012341234"));
        //when
        Member findMember = queryFactory.selectFrom(member)
                .where(member.nickname.eq(nickname)).fetchFirst();

        boolean existsIsTrue = findMember == null;

        //then
        assertThat(existsIsTrue).isFalse();
        assertThat(findMember.getNickname()).isEqualTo(nickname);
    }

    @Test
    void 가입된_로그인아이디가_존재하지않을경우_True_반환() {

        //given
        String loginId = "test12";

        //when
        Member findMember = queryFactory.selectFrom(member)
                .where(member.loginId.eq(loginId))
                .fetchFirst();

        boolean isExists = findMember == null;

        //then
        assertThat(isExists).isTrue();
        assertThat(findMember).isNull();
    }

    @Test
    void 가입된_로그인아이디가_존재하지할경우_False_반환() {

        //given
        String loginId = "test12";
        memberRepository.save(
                new Member("test12", "1234", "hello",
                        "아으닉네임", "19981015", "01012341234"));

        //when
        Member findMember = queryFactory.selectFrom(member)
                .where(member.loginId.eq(loginId))
                .fetchFirst();

        boolean isExists = findMember == null;

        //then
        assertThat(isExists).isFalse();
        assertThat(findMember.getLoginId()).isEqualTo(loginId);
    }

    @Test
    void 해당닉네임을가진_유저_반환() {

        //given
        String nickname = "아으닉네임";
        memberRepository.save(
                new Member("hoe", "1234", "hello",
                        "아으닉네임", "19981015", "01012341234"));

        //when
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.nickname.eq(nickname))
                .fetchOne();

        //then
        assertThat(findMember.getNickname()).isEqualTo(nickname);
        assertThat(findMember.getLoginId()).isEqualTo("hoe");
    }


}