package com.back.moyeomoyeo.errorhandle.friend;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class FriendExceptionResponse {
    private final int status;
    private final LocalDateTime currentTime = LocalDateTime.now();
    private final String error;
    private final String code;
    private final String message;


    public static ResponseEntity<FriendExceptionResponse> toResponseEntity(FriendErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(FriendExceptionResponse.builder()
                        .code(errorCode.name())
                        .status(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .error(errorCode.getHttpStatus().name())
                        .build());
    }
}
