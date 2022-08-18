package com.back.moyeomoyeo.repository.member;

import com.back.moyeomoyeo.entity.member.Member;

public interface MemberRepositoryCustom {
    boolean existsMemberLoginIdOrNickname(String loginId, String nickname);

    boolean existsLoginId(String loginId);

    boolean existsNickname(String nickname);

    Member findByNickname(String nickname);
}
