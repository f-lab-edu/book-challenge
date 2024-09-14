package com.flab.book_challenge.book;

import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.response.BooksPaginationNoOffsetResponse;
import com.flab.book_challenge.book.response.BooksPaginationOffsetResponse;
import java.util.List;
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
            .createAt(book.getCreatedAt())
            .build();
    }

    public static BooksPaginationOffsetResponse toPaginationResponse(Page<Book> books) {
        return BooksPaginationOffsetResponse.builder()
            .pageNumber(books.getPageable().getPageNumber())
            .size(books.getSize())
            .totalElementSize(books.getTotalElements())
            .totalPageSize(books.getTotalPages())
            .hasNext(books.hasNext())
            .data(books.getContent().stream()
                .map(BookMapper::toResponse)
                .toList()
            )
            .build();
    }

    public static BooksPaginationNoOffsetResponse toPaginationResponse(List<Book> books, String next) {
        return BooksPaginationNoOffsetResponse.builder()
            .data(books.stream()
                .map(BookMapper::toResponse)
                .toList()
            )
            .next(next)
            .build();
    }


}
