package com.back.moyeomoyeo.repository.member;

import com.back.moyeomoyeo.entity.member.Member;

public interface MemberRepositoryCustom {
    boolean existsMemberLoginIdOrNickname(String loginId, String nickname);

    boolean existsLoginId(String loginId);


    Member findByNickname(String nickname);
}
