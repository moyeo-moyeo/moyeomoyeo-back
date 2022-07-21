package com.back.moyeomoyeo.security;

import com.back.moyeomoyeo.entity.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class AuthorizedUser implements UserDetails, OAuth2User {
    private Member member;
    private Map<String, Object> attributes;

    public AuthorizedUser(Member member, Map<String, Object> attributes) {
        this.attributes = attributes;
        this.member = member;
    }


    public AuthorizedUser(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().toString();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        String sub = attributes.get("sub").toString();
        return sub;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
