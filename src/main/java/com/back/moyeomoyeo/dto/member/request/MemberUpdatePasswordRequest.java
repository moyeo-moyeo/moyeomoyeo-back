package com.back.moyeomoyeo.dto.member.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdatePasswordRequest {
    private String beforePassword;
    private String afterPassword;
    private String repeatPassword;



    public MemberUpdatePasswordRequest(String beforePassword, String afterPassword, String repeatPassword) {
        this.beforePassword = beforePassword;
        this.afterPassword = afterPassword;
        this.repeatPassword = repeatPassword;
    }
}