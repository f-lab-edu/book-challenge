package com.flab.book_challenge.common.exception;

import lombok.Getter;

@Getter
public enum ErrorStatus {

    // COMMON
    QUERY_NOT_FOUND("COMMON_0", "isbn이나 name을 입력해주세요"),

    // BOOK
    BOOK_DUPLICATION("BOOK_0", "이미 존재하는 책입니다."),
    BOOK_NOT_FOUND("BOOK_1", "책을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ErrorStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
