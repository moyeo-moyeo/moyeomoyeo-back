package com.back.moyeomoyeo.errorhandle.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_LOGINID(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    DUPLICATE_LOGINID_OR_NICKNAME(HttpStatus.CONFLICT, "아이디 또는 닉네임 중복여부를 확인해주시기 바랍니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네입입니다."),
    DUPLICATE_ADD_REQUEST_FRIEND(HttpStatus.CONFLICT, "이미 친구 요청을 보낸 사용자입니다."),
    DUPLICATE_FRIEND(HttpStatus.CONFLICT, "이미 친구 추가된 사용자입니다."),

    NOT_MATCH_PASSWORD_CONFIRM_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 재입력이 일치하지 않습니다."),
    NOT_ADD_MYSELF(HttpStatus.BAD_REQUEST, "본인을 친구 추가할 수 없습니다."),
    NOT_EXISTS_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
