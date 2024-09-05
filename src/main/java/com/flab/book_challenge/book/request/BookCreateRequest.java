package com.flab.book_challenge.book.request;

import jakarta.validation.constraints.NotBlank;

public record BookCreateRequest(
    @NotBlank String bookCode,
    @NotBlank String name,
    int pageCount) {

}
