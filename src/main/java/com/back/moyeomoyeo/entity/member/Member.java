package com.back.moyeomoyeo.entity.member;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId;

    private String password;

    private String username;

    private String nickname;

    private String birthDate;

    private String phoneNumber;

    private String role;

    public void encodingPassword(String password) {
        this.password = password;
    }

    public List<String> getRoleList(){
        if(this.role.length()>0)
            return Arrays.asList(role.split(","));

        return new ArrayList<>();
    }
    public Member(String loginId, String password, String username, String nickname, String birthDate, String phoneNumber) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.role = "ROLE_USER";
    }
}
