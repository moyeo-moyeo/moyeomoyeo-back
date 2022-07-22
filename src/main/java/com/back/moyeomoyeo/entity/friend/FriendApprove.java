package com.back.moyeomoyeo.entity.friend;

import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendProcessEnum;
import com.back.moyeomoyeo.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class FriendApprove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendApproveId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    private String requestNickname;

    @Enumerated(EnumType.STRING)
    private FriendApproveEnum isApprove;

    @Enumerated(EnumType.STRING)
    private FriendProcessEnum isProcess;

    public FriendApprove(Member member, String requestNickname) {
        this.member = member;
        this.requestNickname = requestNickname;
        this.isApprove = FriendApproveEnum.REQUEST;
        this.isProcess = FriendProcessEnum.WAIT;
    }

    public void processFriendStatus(FriendApproveEnum isApprove, FriendProcessEnum isProcess) {
        this.isApprove = isApprove;
        this.isProcess = isProcess;
    }
}
