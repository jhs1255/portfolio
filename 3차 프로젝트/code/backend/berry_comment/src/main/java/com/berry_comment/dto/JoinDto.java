package com.berry_comment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinDto {

    //가입 아이디
    @NotNull
    private String id;

    //가입 이메일
    @Email
    private String email;

    //가입 이름
    @NotNull
    private String name;

    //가입 비밀번호
    @NotNull
    private String password;
}
