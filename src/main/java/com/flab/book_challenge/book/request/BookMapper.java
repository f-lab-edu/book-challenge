package com.flab.book_challenge.book.request;

import com.flab.book_challenge.book.domain.Book;

public final class BookMapper {

    private BookMapper() {
    }

    public static Book toEntity(String isbn, String name, int pageCount) {
        return Book.builder()
            .isbn(isbn)
            .name(name)
            .pageCount(pageCount)
            .build();
    }
}
