package com.flab.book_challenge.book.service;

import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookSearchRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.response.BooksResponse;
import java.util.List;

public interface BookService {

    BooksResponse getBooks(int page, int size);

    List<BookDetailResponse> searchBooks(BookSearchRequest bookSearchRequest);

    BookDetailResponse getBookByBookCode(String bookCode);

    List<BookDetailResponse> getBooksByName(String name);

    long addBook(BookCreateRequest bookCreateRequest);

    long updateBook(BookUpdateRequest updateRequest);

    void deleteBook(long saveBookId);
}
