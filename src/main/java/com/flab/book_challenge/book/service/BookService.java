package com.flab.book_challenge.book.service;

import com.flab.book_challenge.book.request.BookCreateRequest;

public interface BookService {
    void addBook(BookCreateRequest bookCreateRequest);
}
