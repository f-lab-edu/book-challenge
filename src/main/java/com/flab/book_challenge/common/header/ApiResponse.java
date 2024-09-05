package com.flab.book_challenge.common.header;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorResponse error;

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.error = null;
    }

    public ApiResponse(ErrorResponse error) {
        this.success = false;
        this.data = null;
        this.error = error;
    }
}
