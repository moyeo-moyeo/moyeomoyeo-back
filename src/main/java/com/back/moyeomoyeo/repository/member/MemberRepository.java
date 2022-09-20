package com.back.moyeomoyeo.repository.member;

import com.back.moyeomoyeo.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Transactional
    Member findByLoginId(String loginId);

    Member findByNickname(String nickname);

    Boolean existsByLoginId(String loginId);
}
