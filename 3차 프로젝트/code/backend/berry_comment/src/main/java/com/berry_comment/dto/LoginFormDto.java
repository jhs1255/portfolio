package com.berry_comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class LoginFormDto {
    @NotNull
    //유저 아이디
    private String id;

    //유저 비밀번호
    @NotNull
    private String password;
}
