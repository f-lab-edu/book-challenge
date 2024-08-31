package com.flab.book_challenge.book.service;

import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import java.util.List;

public interface BookService {
    void addBook(BookCreateRequest bookCreateRequest);

    List<BookDetailResponse> getBooks();

    BookDetailResponse getBookByIsbn(String isbn);

    List<BookDetailResponse> getBooksByName(String name);
}
