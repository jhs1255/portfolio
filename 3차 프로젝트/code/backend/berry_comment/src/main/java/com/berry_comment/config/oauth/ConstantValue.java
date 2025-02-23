package com.berry_comment.config.oauth;

import java.time.Duration;

public class ConstantValue {
    public static final String REFRESH_TOKEN = "refresh_token";

    public static final String ACCESS_TOKEN = "access_token";

    //액세스 토큰 유효시간(60분)
    public static final Duration ACCESS_TOKEN_EXPIRE = Duration.ofMinutes(60);

    //리프레시 토큰 유효시간
    public static final Duration REFRESH_TOKEN_EXPIRE = Duration.ofDays(1);
}
