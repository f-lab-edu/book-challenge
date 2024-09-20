package com.flab.book_challenge.book.repository;

import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.request.BookSearchRequest;
import com.flab.book_challenge.book.service.SortCondition;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<Book> paginationBookSearch(Pageable pageable, BookSearchRequest searchRequest);

    List<Book> findBooksNoOffset(SortCondition sortCondition, int limit);
}
