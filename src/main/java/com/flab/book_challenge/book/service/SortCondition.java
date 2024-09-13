package com.flab.book_challenge.book.service;

import com.flab.book_challenge.book.controller.BookSortType;


public record SortCondition(boolean isAscending, BookSortType sortField, String lastValue) {

    public static SortCondition by(boolean isAscending, BookSortType sortField, String lastValue) {
        return new SortCondition(isAscending, sortField, lastValue);
    }

}
