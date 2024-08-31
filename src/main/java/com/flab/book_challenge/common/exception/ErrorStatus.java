package com.flab.book_challenge.common.exception;

public enum ErrorStatus {

    // BOOK
    BOOK_NOT_FOUND("책을 찾을 수 없습니다.");

    private final String message;

    ErrorStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
