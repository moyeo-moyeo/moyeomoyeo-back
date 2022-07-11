package com.back.moyeomoyeo.service.member;

import com.back.moyeomoyeo.dto.member.request.MemberRequest;
import com.back.moyeomoyeo.dto.member.response.MemberDuplicateResponse;
import com.back.moyeomoyeo.dto.member.response.MemberResponse;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorCode;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public MemberResponse newUser(MemberRequest memberRequest) {
        if(memberRepository.existsByLoginIdOrNickname(memberRequest.getLoginId(), memberRequest.getNickname())) {
            throw new ErrorException(ErrorCode.DUPLICATE_LOGINID_OR_NICKNAME);
        }
        if(!memberRequest.getPassword().equals(memberRequest.getConfirmPassword())) {
            throw new ErrorException(ErrorCode.NOT_MATCH_PASSWORD_CONFIRM_PASSWORD);
        }
        Member member = memberRequest.toEntity();
        member.encodingPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return new MemberResponse(member.getId(),"회원가입 성공");
    }

    @Override
    public MemberDuplicateResponse isLoginId(String loginId) {
        if(memberRepository.existsByLoginId(loginId)) {
            throw new ErrorException(ErrorCode.DUPLICATE_LOGINID);
        }
        return new MemberDuplicateResponse("사용 가능한 아이디입니다.");
    }

    @Override
    public MemberDuplicateResponse isNickname(String nickname) {
        if(memberRepository.existsByNickname(nickname)) {
            throw new ErrorException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return new MemberDuplicateResponse("사용 가능한 닉네임입니다.");
    }
}
