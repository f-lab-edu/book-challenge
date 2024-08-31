package com.flab.book_challenge.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus {

    // COMMON
    QUERY_NOT_FOUND(NOT_FOUND, "COMMON_0", "isbn이나 name을 입력해주세요"),

    // BOOK
    BOOK_DUPLICATION(BAD_REQUEST, "BOOK_0", "이미 존재하는 책입니다."),
    BOOK_NOT_FOUND(NOT_FOUND, "BOOK_1", "책을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public static HttpStatus getHttpStatusByCode(String code) {
        for (ErrorStatus errorStatus : values()) {
            if (errorStatus.getCode().equals(code)) {
                return errorStatus.getHttpStatus();
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    ErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
