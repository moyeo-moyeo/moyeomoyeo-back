package com.back.moyeomoyeo.security;

import com.back.moyeomoyeo.entity.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class AuthorizedUser extends User {
    private Member member;


    public AuthorizedUser(Member member) {
        super(member.getLoginId(), member.getPassword(), AuthorityUtils.createAuthorityList(member.getRoleList().toString()));
        this.member= member;
    }

}
