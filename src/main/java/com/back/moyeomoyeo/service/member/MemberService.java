package com.back.moyeomoyeo.service.member;

import com.back.moyeomoyeo.dto.member.request.MemberRequest;
import com.back.moyeomoyeo.dto.member.request.MemberUpdatePasswordRequest;
import com.back.moyeomoyeo.dto.member.response.MemberDuplicateResponse;
import com.back.moyeomoyeo.dto.member.response.MemberIssueTempNumberResponse;
import com.back.moyeomoyeo.dto.member.response.MemberResponse;
import com.back.moyeomoyeo.dto.member.response.MemberUpdatePasswordResponse;
import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.errorhandle.member.ErrorCode;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.repository.member.MemberRepositoryCustom;
import com.back.moyeomoyeo.security.AuthorizedUser;
import com.back.moyeomoyeo.service.tempNumber.TempNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TempNumberService tempNumberService;


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

    @Transactional
    public MemberUpdatePasswordResponse changePassword(MemberUpdatePasswordRequest memberUpdatePasswordRequest) {
        AuthorizedUser authorizedUser = this.sessionUser();
        Member byLoginId = memberRepository.findByLoginId(authorizedUser.getUsername());

        if (this.isAuthorizedPassword(memberUpdatePasswordRequest.getBeforePassword())) {
            String encodedPassword = bCryptPasswordEncoder.encode(memberUpdatePasswordRequest.getAfterPassword());
            byLoginId.encodingPassword(encodedPassword);

            return new MemberUpdatePasswordResponse(memberUpdatePasswordRequest.getBeforePassword(), byLoginId.getPassword());
        }
        return new MemberUpdatePasswordResponse();
    }

    @Transactional
    public MemberUpdatePasswordResponse doUpdateTempPassword() {

        AuthorizedUser authorizedUser = this.sessionUser();
        Member byLoginId = memberRepository.findByLoginId(authorizedUser.getUsername());

        String temporaryPassword = tempNumberService.createTemporaryNumber();
        System.out.println("temporaryPassword = " + temporaryPassword);
        String encodedTemporaryPassword = bCryptPasswordEncoder.encode(temporaryPassword);

        MemberUpdatePasswordResponse response = new MemberUpdatePasswordResponse(byLoginId.getPassword(), "");
        byLoginId.encodingPassword(encodedTemporaryPassword);
        response.setCurrentPassword(byLoginId.getPassword());

        return response;

    }
    MemberIssueTempNumberResponse reqIssueTempNumber(String reqUser) {
        Boolean existsMember = memberRepository.existsByLoginId(reqUser);

        if(!existsMember)
            throw new ErrorException(ErrorCode.NOT_EXISTS_USER);


        return new MemberIssueTempNumberResponse(tempNumberService.savedTempNumber());
    }
    protected AuthorizedUser sessionUser() {
        return (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String createTemporaryNumber() {

        return tempNumberService.createTemporaryNumber();
    }

    public boolean isAuthorizedPassword(String reqPassword) {
        AuthorizedUser authorizedUser = this.sessionUser();
        if (bCryptPasswordEncoder.matches(reqPassword, authorizedUser.getPassword()))
            return true;
        return false;
    }


}

