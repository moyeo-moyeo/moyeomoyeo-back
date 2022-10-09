package com.back.moyeomoyeo.dto.friend.request;

import com.back.moyeomoyeo.entity.friend.FriendApprove;
import com.back.moyeomoyeo.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class NewFriendRequest {
    private String friendNickname;


    public FriendApprove toEntity(Member member, String friendNickname) {
        return new FriendApprove(member, friendNickname);
    }
}
