package com.flab.book_challenge.book;

import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.response.BookDetailResponse;

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

    public static BookDetailResponse toResponse(long id, String isbn, String name, int pageCount) {
        return new BookDetailResponse(id, isbn, name, pageCount);
    }
}
