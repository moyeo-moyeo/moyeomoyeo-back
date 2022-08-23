package com.back.moyeomoyeo.errorhandle.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FriendErrorException extends RuntimeException {
    private final FriendErrorCode friendErrorCode;
}
