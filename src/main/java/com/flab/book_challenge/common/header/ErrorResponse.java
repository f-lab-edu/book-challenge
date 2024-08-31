package com.flab.book_challenge.common.header;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
