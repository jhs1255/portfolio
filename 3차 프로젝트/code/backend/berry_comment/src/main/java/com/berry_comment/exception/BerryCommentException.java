package com.berry_comment.exception;

import lombok.Getter;

@Getter
public class BerryCommentException extends RuntimeException {
    private ErrorCode errorCode;
    private String detailMessage;

    public BerryCommentException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public BerryCommentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }
}
