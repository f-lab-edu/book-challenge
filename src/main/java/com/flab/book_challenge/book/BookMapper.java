package com.flab.book_challenge.book;

import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.response.BookDetailResponse;

public final class BookMapper {

    private BookMapper() {
    }

    public static Book toEntity(String bookCode, String name, int pageCount) {
        return Book.builder()
            .bookCode(bookCode)
            .name(name)
            .pageCount(pageCount)
            .build();
    }

    public static BookDetailResponse toResponse(long id, String bookCode, String name, int pageCount) {
        return new BookDetailResponse(id, bookCode, name, pageCount);
    }
}
