package com.berry_comment.exception;

import com.berry_comment.dto.BerryCommentErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@RestControllerAdvice
@Slf4j
public class BerryCommentExceptionHandler {
    //예외처리를 감지하는 핸들러
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BerryCommentException.class)
    @ResponseBody
    public BerryCommentErrorResponse handleException(BerryCommentException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return BerryCommentErrorResponse
                .builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getMessage())
                .build();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public BerryCommentErrorResponse handleException(EntityNotFoundException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return BerryCommentErrorResponse.builder()
                .errorMessage(e.getMessage())
                .errorCode(ErrorCode.NOT_FOUND)
                .build();
    }
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public BerryCommentErrorResponse handleException(AuthorizationDeniedException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return BerryCommentErrorResponse.builder()
                .errorCode(ErrorCode.NOT_AUTHORIZATION)
                .build();
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = DuplicateKeyException.class)
    public BerryCommentErrorResponse handleException(DuplicateKeyException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return BerryCommentErrorResponse.builder()
                .errorMessage(e.getMessage())
                .errorCode(ErrorCode.DUPLICATE_USER)
                .build();
    }
}
