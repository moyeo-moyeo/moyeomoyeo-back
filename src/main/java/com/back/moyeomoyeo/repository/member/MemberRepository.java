package com.back.moyeomoyeo.repository.member;

import com.back.moyeomoyeo.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByLoginId(String loginId);

    Member findByNickname(String nickname);

    Boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);
}
