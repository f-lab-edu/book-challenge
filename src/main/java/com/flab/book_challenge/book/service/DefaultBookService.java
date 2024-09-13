package com.flab.book_challenge.book.service;

import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_DUPLICATION;
import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_NOT_FOUND;
import static com.flab.book_challenge.common.exception.ErrorStatus.QUERY_NOT_FOUND;

import ch.qos.logback.core.util.StringUtil;
import com.flab.book_challenge.book.BookMapper;
import com.flab.book_challenge.book.controller.BookSortType;
import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.repository.BookRepository;
import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookSearchRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.response.BooksPaginationNoOffsetResponse;
import com.flab.book_challenge.book.response.BooksPaginationOffsetResponse;
import com.flab.book_challenge.common.exception.GeneralException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultBookService implements BookService {
    private final BookRepository bookRepository;


    @Override
    public List<BookDetailResponse> searchBooks(BookSearchRequest searchRequest) {

        if (StringUtil.notNullNorEmpty(searchRequest.getBookCode())) {
            return List.of(getBookByBookCode(searchRequest.getBookCode()));
        }
        else if (StringUtil.notNullNorEmpty(searchRequest.getName())) {
            return getBooksByName(searchRequest.getName());
        }

        throw new GeneralException(QUERY_NOT_FOUND);
    }

    @Override
    public BookDetailResponse getBookById(Long id) {
        return bookRepository.findById(id).map(BookMapper::toResponse)
            .orElseThrow(() -> new GeneralException(BOOK_NOT_FOUND));
    }

    @Override
    public BooksPaginationOffsetResponse getBooksByPaginationLegacy(Pageable pageable, BookSortType sortType) {
        Page<Book> bookPage = bookRepository.findAll(getPageableWithSort(pageable, sortType));
        return BookMapper.toPaginationResponse(bookPage);
    }

    @Override
    public BooksPaginationNoOffsetResponse getBooksByPaginationNoOffset(String sort, String lastValue,
                                                                        boolean isAscending, int limit) {
        SortCondition<?> sortCondition = this.createSortCondition(BookSortType.from(sort), lastValue, isAscending);
        List<Book> books = bookRepository.findBooksNoOffset(sortCondition, limit);

        String next = null;
        if (!books.isEmpty()) {
            Book lastBook = books.getLast();
            String nextLastValue = getNextLastValue(lastBook, BookSortType.from(sort));
            next = buildNextPageUrl(sort, nextLastValue, isAscending, limit);
        }

        return BookMapper.toPaginationResponse(books, next);

    }

    // bookCode 조회는 단일 책을, 정확한 이름 검색은 책 목록을 반환합니다.
    @Override
    public BookDetailResponse getBookByBookCode(String bookCode) {
        return bookRepository.findBookByBookCode(bookCode)
            .map(BookMapper::toResponse)
            .orElseThrow(() -> new GeneralException(BOOK_NOT_FOUND));
    }

    @Override
    public List<BookDetailResponse> getBooksByName(String name) {
        return bookRepository.findBooksByNameContaining(name).stream()
            .map(BookMapper::toResponse)
            .toList();
    }


    @Transactional
    @Override
    public long addBook(BookCreateRequest bookRequest) {
        existsBookByBookCode(bookRequest.bookCode());
        return bookRepository.save(
            BookMapper.toEntity(bookRequest.bookCode(), bookRequest.name(), bookRequest.pageCount())).getId();
    }

    @Transactional
    @Override
    public long updateBook(BookUpdateRequest updateRequest) {
        Book book = existsBookById(updateRequest.id());
        if (!book.getBookCode().equals(updateRequest.bookCode())) {
            existsBookByBookCode(updateRequest.bookCode());
        }

        return updateBook(book, updateRequest).getId();
    }

    @Transactional
    @Override
    public void deleteBook(long bookId) {
        Book book = existsBookById(bookId);
        bookRepository.delete(book);
    }

    private Pageable getPageableWithSort(Pageable pageable, BookSortType sortType) {
        Sort sort = BookSortType.getSort(sortType);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    private Book existsBookById(long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new GeneralException(BOOK_NOT_FOUND));
    }

    private void existsBookByBookCode(String bookCode) {
        bookRepository.findBookByBookCode(bookCode)
            .ifPresent(b -> {
                throw new GeneralException(BOOK_DUPLICATION);
            });
    }

    private Book updateBook(Book preBook, BookUpdateRequest updateRequest) {
        preBook.updateBookCode(updateRequest.bookCode());
        preBook.updateName(updateRequest.name());
        preBook.updatePageCount(updateRequest.pageCount());

        return bookRepository.save(preBook);
    }


    private SortCondition<?> createSortCondition(BookSortType sortType, String lastValue, boolean isAscending) {
        return SortCondition.by(isAscending, sortType, lastValue);
    }

    private String getNextLastValue(Book lastBook, BookSortType sortType) {
        return switch (sortType) {
            case CREATED_AT -> lastBook.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            case BOOK_NAME -> lastBook.getName();
            case PAGE_COUNT -> String.valueOf(lastBook.getPageCount());
            default -> throw new IllegalArgumentException("Unsupported sort type: " + sortType);
        };
    }

    private String buildNextPageUrl(String sort, String lastValue, boolean isAscending, int limit) {
        return UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1/books")
            .queryParam("sortType", sort)
            .queryParam("lastValue", URLEncoder.encode(lastValue, StandardCharsets.UTF_8))
            .queryParam("isAscending", isAscending)
            .queryParam("pageSize", limit)
            .build()
            .toUriString();
    }
}
