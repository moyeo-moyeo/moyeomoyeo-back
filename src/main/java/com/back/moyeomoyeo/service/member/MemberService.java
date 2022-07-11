package com.back.moyeomoyeo.service.member;

import com.back.moyeomoyeo.dto.member.request.MemberRequest;
import com.back.moyeomoyeo.dto.member.response.MemberDuplicateResponse;
import com.back.moyeomoyeo.dto.member.response.MemberResponse;

public interface MemberService {

    MemberResponse newUser(MemberRequest memberRequest);

    MemberDuplicateResponse isLoginId(String loginId);

    MemberDuplicateResponse isNickname(String nickname);
}
