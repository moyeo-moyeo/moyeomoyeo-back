package com.back.moyeomoyeo.dto.friend.request;

import com.back.moyeomoyeo.entity.friend.Friend;
import com.back.moyeomoyeo.entity.friend.friendenum.FriendApproveEnum;
import com.back.moyeomoyeo.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewFriendReqProcessRequest {

    private String friendNickname;

    private FriendApproveEnum isApprove;

    public Friend toEntity(Member member, String friendNickname) {
        return new Friend(member, friendNickname);
    }

}
