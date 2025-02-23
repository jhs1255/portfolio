package com.berry_comment.type;

//oauth 유저인가, 일반유저인가 판별

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeUser {
    GOOGLE_USER("google"),
    NAVER_USER("naver"),
    KAKAO_USER("kakao"),
    NORMAL_USER("normal"),;
    private final String type;
}
