package com.flab.book_challenge.book.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookSearchRequest {
    private String bookCode;
    private String name;

}
