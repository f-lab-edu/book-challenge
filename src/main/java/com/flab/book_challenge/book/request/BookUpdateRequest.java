package com.flab.book_challenge.book.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookUpdateRequest(
    @NotNull Long id,
    @NotBlank String bookCode,
    @NotBlank String name,
    int pageCount) {
}
