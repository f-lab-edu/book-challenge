package com.flab.book_challenge.book.service;

import com.flab.book_challenge.book.controller.BookSortType;
import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookSearchRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.response.BooksPaginationNoOffsetResponse;
import com.flab.book_challenge.book.response.BooksPaginationOffsetResponse;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDetailResponse getBookById(Long id);

    BooksPaginationOffsetResponse getBooksByPaginationLegacy(Pageable pageable, BookSortType sortType);

    BooksPaginationOffsetResponse searchBooks(Pageable pageable, BookSearchRequest bookSearchRequest);

    BooksPaginationNoOffsetResponse getBooksByPaginationNoOffset(String sort, String lastValue, boolean isAscending,
                                                                 int limit);

    long addBook(BookCreateRequest bookCreateRequest);

    long updateBook(BookUpdateRequest updateRequest);

    void deleteBook(long saveBookId);
}
