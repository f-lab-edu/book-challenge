package com.flab.book_challenge.book.service;

import com.flab.book_challenge.book.controller.BookSortType;


public record SortCondition<T>(boolean isAscending, BookSortType sortField, T lastValue) {

    public static <T> SortCondition<T> by(boolean isAscending, BookSortType sortField, T lastValue) {
        return new SortCondition<>(isAscending, sortField, lastValue);
    }

}
