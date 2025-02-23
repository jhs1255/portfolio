package com.berry_comment.dto;

import com.berry_comment.exception.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BerryCommentErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
