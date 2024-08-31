package com.flab.book_challenge.book.request;

public record BookUpdateRequest(long id, String isbn, String name, int pageCount) {
}
