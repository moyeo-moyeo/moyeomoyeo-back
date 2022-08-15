package com.back.moyeomoyeo.dto.member.response;

import com.back.moyeomoyeo.dto.tempNumber.response.SavedTempNumberResponse;
import lombok.Getter;

@Getter
public class MemberIssueTempNumberResponse {
    private String reqUser;
    private String tempNumber;
    private String message;

    public MemberIssueTempNumberResponse(SavedTempNumberResponse response) {
        this.reqUser = response.getReqUser();
        this.tempNumber = response.getTempNumber();
        this.message = "임시번호가 발급되었습니다";
    }


}
