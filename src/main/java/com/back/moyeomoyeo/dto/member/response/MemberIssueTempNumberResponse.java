package com.back.moyeomoyeo.dto.member.response;

public class MemberIssueTempNumberResponse {
    private String tempNumber;
    private String message;

    public MemberIssueTempNumberResponse(String tempNumber) {
        this.tempNumber = tempNumber;
        this.message = "임시번호가 발급되었습니다";
    }

    public String getTempNumber() {
        return tempNumber;
    }

    public void setTempNumber(String tempNumber) {
        this.tempNumber = tempNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
