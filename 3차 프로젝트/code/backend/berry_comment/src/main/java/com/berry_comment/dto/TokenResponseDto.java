package com.berry_comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
