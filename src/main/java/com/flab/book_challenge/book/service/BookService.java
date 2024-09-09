package com.flab.book_challenge.book.service;

import com.flab.book_challenge.book.controller.BookSortType;
import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookSearchRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.response.BooksResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDetailResponse getBookById(Long id);

    BooksResponse getBooks(Pageable pageable, BookSortType sortType);

    List<BookDetailResponse> searchBooks(BookSearchRequest bookSearchRequest);

    BookDetailResponse getBookByBookCode(String bookCode);

    List<BookDetailResponse> getBooksByName(String name);

    long addBook(BookCreateRequest bookCreateRequest);

    long updateBook(BookUpdateRequest updateRequest);

    void deleteBook(long saveBookId);
}
