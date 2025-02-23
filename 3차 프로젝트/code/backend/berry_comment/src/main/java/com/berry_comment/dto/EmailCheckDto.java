package com.berry_comment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class EmailCheckDto {
    @Email
    private String email;

    @NotEmpty
    private String password;
}
