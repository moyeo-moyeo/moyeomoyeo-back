package com.back.moyeomoyeo.errorhandle.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FriendErrorCode {

    NOT_FRIEND_EXISTS(HttpStatus.BAD_REQUEST, "친구 추가 안된 유저입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
