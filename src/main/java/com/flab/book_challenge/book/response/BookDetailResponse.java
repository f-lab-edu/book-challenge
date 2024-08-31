package com.flab.book_challenge.book.response;

public record BookDetailResponse(
    long id,
    String isbn,
    String name,
    int pageCount
) {
}
