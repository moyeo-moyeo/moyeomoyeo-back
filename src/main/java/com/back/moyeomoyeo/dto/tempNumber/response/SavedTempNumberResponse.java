package com.back.moyeomoyeo.dto.tempNumber.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SavedTempNumberResponse {
    protected String reqUser;
    protected String tempNumber;
}
