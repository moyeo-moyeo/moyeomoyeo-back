package com.back.moyeomoyeo.entity.promise;

import com.back.moyeomoyeo.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PromisePlace extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promisePlaceId")
    private Long id;

    private String placeName; // 장소이름

    private Integer latitude; // 위도


    private Integer longitude; // 경도

    private String meetingDate;

    public PromisePlace(String placeName, Integer latitude, Integer longitude, String meetingDate) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.meetingDate = meetingDate;
    }
}
