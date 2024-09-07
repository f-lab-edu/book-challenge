package com.flab.book_challenge.book.response;

import lombok.Builder;

@Builder
public record BookDetailResponse(
    long id,
    String bookCode,
    String name,
    int pageCount
) {
}
