package com.back.moyeomoyeo.entity.friend;

import com.back.moyeomoyeo.BaseEntity;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendProcessEnum;
import com.back.moyeomoyeo.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class FriendApprove extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendApproveId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member; // 친구 요청을 보낸 사람

    private String requestNickname; // 친구 요청을 받은 사람

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
