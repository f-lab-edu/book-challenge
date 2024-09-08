package com.flab.book_challenge.common.header;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T content;
    private final ErrorResponse error;

    public ApiResponse(T data) {
        this.success = true;
        this.content = data;
        this.error = null;
    }

    public ApiResponse(ErrorResponse error) {
        this.success = false;
        this.content = null;
        this.error = error;
    }
}
