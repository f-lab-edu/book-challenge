package com.flab.book_challenge.book.repository;

import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_SORT_NOT_FOUND;

import ch.qos.logback.core.util.StringUtil;
import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.domain.QBook;
import com.flab.book_challenge.book.request.BookSearchRequest;
import com.flab.book_challenge.book.service.SortCondition;
import com.flab.book_challenge.common.exception.GeneralException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Book> paginationBookSearch(Pageable pageable, BookSearchRequest searchRequest) {
        QBook book = QBook.book;

        Predicate predicate = createSearchPredicate(book, searchRequest);

        List<Book> books = queryFactory
            .selectFrom(book)
            .where(predicate)
            .orderBy(book.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(book.count())
            .from(book)
            .where(predicate)
            .fetchOne();

        return new PageImpl<>(books, pageable, total);
    }

    private Predicate createSearchPredicate(QBook book, BookSearchRequest searchRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtil.notNullNorEmpty(searchRequest.getBookCode())) {
            builder.and(
                book.bookCode.like("%" + searchRequest.getBookCode() + "%")
            );
        }

        if (StringUtil.notNullNorEmpty(searchRequest.getName())) {
            builder.and(book.name
                .like("%" + searchRequest.getName() + "%")
            );
        }

        if (searchRequest.getMinPageCount() != null) {
            builder.and(book.pageCount.goe(searchRequest.getMinPageCount()));
        }

        if (searchRequest.getMaxPageCount() != null) {
            builder.and(book.pageCount.loe(searchRequest.getMaxPageCount()));
        }

        return builder.getValue();
    }

    @Override
    public List<Book> findBooksNoOffset(SortCondition sortCondition, int limit) {

        switch (sortCondition.sortField()) {
            case CREATED_AT -> {
                LocalDateTime lastDateTime = LocalDateTime.parse(sortCondition.lastValue(), formatter);
                return findBooksByLocalDateTime(lastDateTime, sortCondition.isAscending(),
                    limit);
            }
            case PAGE_COUNT -> {
                return findBooksByPageCount(Integer.parseInt(sortCondition.lastValue()),
                    sortCondition.isAscending(), limit);
            }
            case BOOK_NAME -> {
                return findBooksByBookName(sortCondition.lastValue(), sortCondition.isAscending(), limit);
            }
            default -> throw new GeneralException(BOOK_SORT_NOT_FOUND);
        }

    }

    private List<Book> findBooksByLocalDateTime(LocalDateTime lastCreatedAt, boolean isAscending, int limit) {
        QBook book = QBook.book;

        BooleanExpression l = isAscending ? book.createdAt.gt(lastCreatedAt) : book.createdAt.lt(lastCreatedAt);
        OrderSpecifier<LocalDateTime> isAsc = isAscending ? book.createdAt.asc() : book.createdAt.desc();

        return queryFactory
            .selectFrom(book)
            .where(l)
            .orderBy(isAsc)
            .limit(limit)
            .fetch();
    }

    private List<Book> findBooksByBookName(String lastBookName, boolean isAscending, int limit) {
        QBook book = QBook.book;

        BooleanExpression l = isAscending ? book.name.gt(lastBookName) : book.name.lt(lastBookName);
        OrderSpecifier<String> isAsc = isAscending ? book.name.asc() : book.name.desc();

        return queryFactory
            .selectFrom(book)
            .where(l)
            .orderBy(isAsc)
            .limit(limit)
            .fetch();
    }

    private List<Book> findBooksByPageCount(int lastPageCount, boolean isAscending, int limit) {
        QBook book = QBook.book;
        BooleanExpression l = isAscending ? book.pageCount.gt(lastPageCount) : book.pageCount.lt(lastPageCount);
        OrderSpecifier<Integer> isAsc = isAscending ? book.pageCount.asc() : book.pageCount.desc();

        return queryFactory
            .selectFrom(book)
            .where(l)
            .orderBy(isAsc)
            .limit(limit)
            .fetch();
    }
}
