package com.back.moyeomoyeo.errorhandle;

import com.back.moyeomoyeo.errorhandle.friend.FriendErrorException;
import com.back.moyeomoyeo.errorhandle.friend.FriendExceptionResponse;
import com.back.moyeomoyeo.errorhandle.member.ErrorException;
import com.back.moyeomoyeo.errorhandle.member.UserExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorRestControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<UserExceptionResponse> userCustomException(ErrorException errorCode) {
        return UserExceptionResponse.toResponseEntity(errorCode.getErrorCode());
    }

    @ExceptionHandler(FriendErrorException.class)
    public ResponseEntity<FriendExceptionResponse> userCustomException(FriendErrorException errorCode) {
        return FriendExceptionResponse.toResponseEntity(errorCode.getFriendErrorCode());
    }
}
