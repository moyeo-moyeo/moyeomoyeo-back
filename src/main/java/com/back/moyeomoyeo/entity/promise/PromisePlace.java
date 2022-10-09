package com.back.moyeomoyeo.entity.promise;

import com.back.moyeomoyeo.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class PromisePlace extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promisePlaceId")
    private Long id;

    @Column(nullable = false)
    private String placeName; // 장소이름

    @Column(nullable = false)
    private Integer latitude; // 위도

    @Column(nullable = false)
    private Integer longitude; // 경도

    @Column(nullable = false)
    private LocalDateTime meetingDate;

    public PromisePlace(String placeName, Integer latitude, Integer longitude, LocalDateTime meetingDate) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.meetingDate = meetingDate;
    }
}
