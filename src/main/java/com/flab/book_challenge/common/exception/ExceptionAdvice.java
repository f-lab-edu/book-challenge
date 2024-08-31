package com.flab.book_challenge.common.exception;

import com.flab.book_challenge.common.header.ApiResponse;
import com.flab.book_challenge.common.header.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<?> handleGeneralException(GeneralException e) {
        log.error("[API ERROR]: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        HttpStatus status = ErrorStatus.getHttpStatusByCode(e.getCode());

        return ResponseEntity.status(status).body(new ApiResponse<>(errorResponse));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        log.error("[Validation ERROR]: {}", errorMessage);

        ErrorResponse errorResponse = new ErrorResponse("INVALID_INPUT", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errorResponse));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));

        log.error("[Constraint violation]: {}", errorMessage);

        ErrorResponse errorResponse = new ErrorResponse("INVALID_INPUT", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e) {
        log.error("[Unexpected error occurred]: ", e);
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(errorResponse));
    }

}
