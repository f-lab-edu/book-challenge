package com.flab.book_challenge.book.repository;

import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.service.SortCondition;
import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findBooksNoOffset(SortCondition sortCondition, int limit);
}
