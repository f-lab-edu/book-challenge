package com.flab.book_challenge.common.exception;

public class GeneralException extends RuntimeException {

    public GeneralException(ErrorStatus status) {
        super(status.getMessage());
    }

    public GeneralException(ErrorStatus status, Throwable cause) {
        super(status.getMessage(), cause);
    }
}
