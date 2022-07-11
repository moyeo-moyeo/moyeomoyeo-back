package com.back.moyeomoyeo.dto.member.request;

import com.back.moyeomoyeo.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberRequest {
    @NotNull(message = "아이디를 필수로 입력하셔야됩니다.")
    private String loginId;

    @NotNull(message = "비밀번호를 필수로 입력하셔야됩니다.")
    private String password;

    @NotNull(message = "비밀번호 재확인을 필수로 입력하셔야됩니다.")
    private String confirmPassword;

    @NotNull(message = "사용자의 이름을 필수로 입력하셔야됩니다.")
    private String username;

    @NotNull(message = "별명을 필수로 입력하셔야됩니다.")
    private String nickname;

    @NotNull(message = "생년월일을 필수로 입력하셔야됩니다.")
    private String birthDate;

    @NotNull(message = "핸드폰 번호를 필수로 입력하셔야됩니다.")
    private String phoneNumber;


    public Member toEntity() {
        return new Member(
                loginId,password,username,nickname,birthDate,phoneNumber
        );
    }
}
