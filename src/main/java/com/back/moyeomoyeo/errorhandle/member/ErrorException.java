package com.back.moyeomoyeo.errorhandle.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorException extends RuntimeException {
    private final ErrorCode errorCode;
}
