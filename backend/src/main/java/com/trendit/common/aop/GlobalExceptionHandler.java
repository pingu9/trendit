package com.trendit.common.aop;

import com.trendit.api.exception.DuplicatedKeywordException;
import com.trendit.api.exception.KeywordHasSpaceException;
import com.trendit.api.exception.PasswordMisMatchException;
import com.trendit.common.exception.BindingException;
import com.trendit.common.exception.IllegalChartDataException;
import com.trendit.common.model.response.BaseRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String BAD_REQUEST_MESSAGE = "입력 오류 발생";
    private final String SERVER_ERROR_MESSAGE = "서버 에러 발생";
    private final String DUPLICATED_KEYWORD_MESSAGE = "중복된 키워드입니다.";

    @ExceptionHandler({NoSuchElementException.class, PasswordMisMatchException.class,
            KeywordHasSpaceException.class, IllegalChartDataException.class, BindingException.class})
    protected ResponseEntity<BaseRes> handleBadRequestExceptions(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(400).body(BaseRes.of(400, BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(DuplicatedKeywordException.class)
    protected ResponseEntity<BaseRes> handleBadRequestExceptions(DuplicatedKeywordException e) {
        e.printStackTrace();
        return ResponseEntity.status(400).body(BaseRes.of(400, DUPLICATED_KEYWORD_MESSAGE));
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    protected ResponseEntity<BaseRes> handleServerSideExceptions(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(BaseRes.of(500, SERVER_ERROR_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseRes> handleOtherExceptions(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(BaseRes.of(500, SERVER_ERROR_MESSAGE));
    }

}
