package com.back.moyeomoyeo.service.member;

import com.back.moyeomoyeo.dto.member.request.MemberRequest;
import com.back.moyeomoyeo.dto.member.response.MemberDuplicateResponse;
import com.back.moyeomoyeo.dto.member.response.MemberResponse;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorCode;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.repository.member.MemberRepositoryCustom;
import com.back.moyeomoyeo.security.AuthorizedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public MemberResponse newUser(MemberRequest memberRequest) {
        if (memberRepositoryCustom.existsMemberLoginIdOrNickname(memberRequest.getLoginId(), memberRequest.getNickname())) {
            throw new ErrorException(ErrorCode.DUPLICATE_LOGINID_OR_NICKNAME);
        }
        if (!memberRequest.getPassword().equals(memberRequest.getConfirmPassword())) {
            throw new ErrorException(ErrorCode.NOT_MATCH_PASSWORD_CONFIRM_PASSWORD);
        }
        Member member = memberRequest.toEntity();
        member.encodingPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return new MemberResponse(member.getId(), "회원가입 성공");
    }

    public MemberDuplicateResponse isLoginId(String loginId) {
        if (memberRepositoryCustom.existsLoginId(loginId)) {
            throw new ErrorException(ErrorCode.DUPLICATE_LOGINID);
        }
        return new MemberDuplicateResponse("사용 가능한 아이디입니다.");
    }

    public MemberDuplicateResponse isNickname(String nickname) {
        if (memberRepositoryCustom.existsNickname(nickname)) {
            throw new ErrorException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return new MemberDuplicateResponse("사용 가능한 닉네임입니다.");
    }

    protected AuthorizedUser sessionUser() {
        return (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String createTemporaryPassword() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        final String passwordList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        StringBuilder sb = new StringBuilder();

        int passwordLength = 8;
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(passwordList.length());
            sb.append(passwordList.charAt(randomIndex));
        }
        return sb.toString();
    }

    public boolean isAuthorizedPassword(String updateReqPassword) {
        AuthorizedUser authorizedUser = this.sessionUser();
        if (bCryptPasswordEncoder.matches(updateReqPassword, authorizedUser.getPassword()))
            return true;
        return false;
    }
}

