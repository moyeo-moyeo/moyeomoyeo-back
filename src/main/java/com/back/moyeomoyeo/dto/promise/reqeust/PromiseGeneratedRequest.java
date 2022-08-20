package com.back.moyeomoyeo.dto.promise.reqeust;

import com.back.moyeomoyeo.entity.promise.PromisePlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PromiseGeneratedRequest {
    private String placeName; // 장소이름

    private Integer latitude; // 위도


    private Integer longitude; // 경도

    private String meetingDate;

    private List<String> friendNicknameList;

    public PromisePlace toEntity() {
        return new PromisePlace(placeName, latitude, longitude, meetingDate);
    }
}
