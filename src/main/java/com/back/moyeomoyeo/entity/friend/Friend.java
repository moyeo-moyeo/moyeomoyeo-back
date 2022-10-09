package com.back.moyeomoyeo.entity.friend;

import com.back.moyeomoyeo.BaseEntity;
import com.back.moyeomoyeo.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Friend extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member mySelfMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member friendMember;

    public Friend(Member mySelfMember, Member friendMember) {
        this.mySelfMember = mySelfMember;
        this.friendMember = friendMember;
    }
}
