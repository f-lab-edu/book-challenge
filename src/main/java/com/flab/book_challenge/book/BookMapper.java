package com.flab.book_challenge.book;

import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.response.BooksResponse;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

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

    public static BookDetailResponse toResponse(Book book) {
        return BookDetailResponse.builder()
            .id(book.getId())
            .bookCode(book.getBookCode())
            .name(book.getName())
            .pageCount(book.getPageCount())
            .build();
    }

    public static BooksResponse toResponse(Page<Book> books) {
        return BooksResponse.builder()
            .pageNumber(books.getPageable().getPageNumber())
            .size(books.getSize())
            .totalElementSize(books.getTotalElements())
            .totalPageSize(books.getTotalPages())
            .hasNext(books.hasNext())
            .data(books.getContent().stream()
                .map(BookMapper::toResponse)
                .collect(Collectors.toList())
            )
            .build();
    }
}
