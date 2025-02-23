package com.berry_comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdRequestDto {

    //유저 가입할때 쓴 이름
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    //유저 가입할때 쓴 이메일
    private String email;
}
