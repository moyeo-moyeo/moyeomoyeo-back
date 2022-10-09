package com.back.moyeomoyeo.entity.promise;

import com.back.moyeomoyeo.BaseEntity;
import com.back.moyeomoyeo.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Promise extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promisePlaceId")
    private PromisePlace promisePlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    private Integer latitude; // 위도


    private Integer longitude; // 경도

    public Promise(PromisePlace promisePlace, Member member) {
        this.promisePlace = promisePlace;
        this.member = member;
    }

    public Promise(PromisePlace promisePlace, Member member, Integer latitude, Integer longitude) {
        this.promisePlace = promisePlace;
        this.member = member;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
