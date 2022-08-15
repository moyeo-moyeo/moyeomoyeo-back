package com.back.moyeomoyeo.dto.member.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdatePasswordResponse {
    private String beforePassword;
    private String currentPassword;

    private String message;



    public MemberUpdatePasswordResponse(String beforePassword, String currentPassword) {
        this.beforePassword = beforePassword;
        this.currentPassword = currentPassword;
        this.message = "임시번호 발급에 성공하였습니다";
    }


}