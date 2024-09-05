package com.flab.book_challenge.book.response;

public record BookDetailResponse(
    long id,
    String bookCode,
    String name,
    int pageCount
) {
}
