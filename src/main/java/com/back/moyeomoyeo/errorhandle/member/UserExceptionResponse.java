package com.back.moyeomoyeo.errorhandle.member;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserExceptionResponse {
    private final int status;
    private final LocalDateTime currentTime = LocalDateTime.now();
    private final String error;
    private final String code;
    private final String message;


    public static ResponseEntity<UserExceptionResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(UserExceptionResponse.builder()
                        .code(errorCode.name())
                        .status(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .error(errorCode.getHttpStatus().name())
                        .build());
    }
}
