package com.back.moyeomoyeo.dto.tempNumber.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SavedTempNumberResponse {
    private String reqUser;
    private String tempNumber;
    private String message = "임시번호 발급에 실패하였습니다";

    public SavedTempNumberResponse(String reqUser, String tempNumber) {
        this.reqUser = reqUser;
        this.tempNumber = tempNumber;
        this.message = "임시번호 발급에 성공하였습니다";
    }
}
