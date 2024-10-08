package com.flab.book_challenge.book.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookSearchRequest {
    private boolean useSearchBtn;
    private String bookCode;
    private String name;
    private Integer minPageCount;
    private Integer maxPageCount;
}
