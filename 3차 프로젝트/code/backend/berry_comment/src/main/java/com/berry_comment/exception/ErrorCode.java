package com.berry_comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TIME_REQUEST("잘못된 시간을 기입하셨습니다."),
    NOT_FOUND("요청하신 정보를 찾을 수 없습니다."),
    NOT_AUTHORIZATION("권한이 없습니다."),
    DUPLICATE_USER("중복된 사용자입니다."),
    ALREADY_PAID("이미 결제를 하셨습니다."),
    TOKEN_INVALID("토큰이 유효하지 않습니다."),
    TOKEN_NOTFOUND("토큰을 찾을 수 없습니다."),
    FORBIDDEN("결제 정보를 찾을 수 없습니다");
    private final String message;
}
