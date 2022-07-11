package com.back.moyeomoyeo.repository.member;

import com.back.moyeomoyeo.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    // 현재 하고있는거 다 querydsl로 수정 할거
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);

    boolean existsByLoginIdOrNickname(String loginId, String nickname);
}
