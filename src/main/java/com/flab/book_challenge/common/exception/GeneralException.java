package com.flab.book_challenge.common.exception;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final String code;

    public GeneralException(ErrorStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public GeneralException(ErrorStatus status, Throwable cause) {
        super(status.getMessage(), cause);
        this.code = status.getCode();
    }
}
