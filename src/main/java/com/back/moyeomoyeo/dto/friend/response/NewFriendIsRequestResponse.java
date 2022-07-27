package com.back.moyeomoyeo.dto.friend.response;

import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendProcessEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewFriendIsRequestResponse {
    private String requestMember;

    private FriendApproveEnum isApprove;
    private FriendProcessEnum isProcess;

    @QueryProjection
    public NewFriendIsRequestResponse(String requestMember, FriendApproveEnum isApprove, FriendProcessEnum isProcess) {
        this.requestMember = requestMember;
        this.isApprove = isApprove;
        this.isProcess = isProcess;
    }
}
