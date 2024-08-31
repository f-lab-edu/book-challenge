package com.flab.book_challenge.book.request;

import jakarta.validation.constraints.NotBlank;

public record BookCreateRequest(
    @NotBlank String isbn,
    @NotBlank String name,
    int pageCount) {

}
