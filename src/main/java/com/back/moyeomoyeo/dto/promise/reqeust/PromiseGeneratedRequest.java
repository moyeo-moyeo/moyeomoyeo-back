package com.back.moyeomoyeo.dto.promise.reqeust;

import com.back.moyeomoyeo.entity.promise.PromisePlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PromiseGeneratedRequest {
    @NotNull(message = "약속장소를 필수로 입력해주세요.")
    private String placeName; // 장소이름

    @NotNull(message = "위도가 필수적으로 필요합니다.")
    private Integer latitude; // 위도

    @NotNull(message = "경도가 필수적으로 필요합니다.")
    private Integer longitude; // 경도

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime meetingDate;

    private List<String> friendNicknameList;

    public PromisePlace toEntity() {
        return new PromisePlace(placeName, latitude, longitude, meetingDate);
    }
}
