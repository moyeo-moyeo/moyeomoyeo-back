package com.back.moyeomoyeo.dto.member.request;

public class MemberUpdatePasswordRequest {
    private String beforePassword;
    private String afterPassword;
    private String repeatPassword;

    public String getBeforePassword() {
        return beforePassword;
    }

    public void setBeforePassword(String beforePassword) {
        this.beforePassword = beforePassword;
    }

    public String getAfterPassword() {
        return afterPassword;
    }

    public void setAfterPassword(String afterPassword) {
        this.afterPassword = afterPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public MemberUpdatePasswordRequest(String beforePassword, String afterPassword, String repeatPassword) {
        this.beforePassword = beforePassword;
        this.afterPassword = afterPassword;
        this.repeatPassword = repeatPassword;
    }
}