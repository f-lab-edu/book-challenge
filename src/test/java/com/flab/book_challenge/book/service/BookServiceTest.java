package com.flab.book_challenge.book.service;

import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_DUPLICATION;
import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.book_challenge.book.controller.BookSortType;
import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookSearchRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.response.BooksPaginationOffsetResponse;
import com.flab.book_challenge.common.exception.GeneralException;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    private BookCreateRequest bookCreateRequest;

    @BeforeEach
    void setUp() {
        String randomBookCode = RandomStringUtils.randomNumeric(13);
        int randomCount = (int) (Math.random() * 101) + 100;

        bookCreateRequest = new BookCreateRequest(randomBookCode, "test_book", randomCount);

    }


    @DisplayName("책 100권 조회")
    @Test
    void getBooks() {
        // given
        addRandomBooks();
        Pageable pageable = PageRequest.of(0, 100);
        BookSortType sortType = BookSortType.LATEST;

        // when
        BooksPaginationOffsetResponse books = bookService.getBooksByPaginationLegacy(pageable, sortType);
        List<BookDetailResponse> booksContent = books.data();

        // then
        int randomIndex = (int) (Math.random() * 100);

        assertThat(booksContent).hasSize(100);
        assertThat(booksContent.get(randomIndex).id()).isNotZero();
        assertThat(booksContent.get(randomIndex).bookCode()).isNotNull();
        assertThat(booksContent.get(randomIndex).name()).isNotNull();
        assertThat(booksContent.get(randomIndex).pageCount()).isNotZero();
    }

    @DisplayName("책 아이디 조회 테스트")
    @Test
    void getBookById() {
        // given
        long saveBookId = bookService.addBook(bookCreateRequest);

        // when
        BookDetailResponse book = bookService.getBookById(saveBookId);

        // then
        assertThat(book.id()).isEqualTo(saveBookId);
        assertThat(book.bookCode()).isEqualTo(bookCreateRequest.bookCode());
        assertThat(book.name()).isEqualTo(bookCreateRequest.name());
        assertThat(book.pageCount()).isEqualTo(bookCreateRequest.pageCount());
    }

    @DisplayName("정상적인 bookCode를 넣었을 때 책 검색 테스트")
    @Test
    void searchBooksByBookCode() {
        // given
        bookService.addBook(bookCreateRequest);
        addRandomBooks();

        BookSearchRequest bookSearchRequest = new BookSearchRequest(false, bookCreateRequest.bookCode(), null, null,
            null);
        Pageable pageable = PageRequest.of(0, 100);

        // when
        List<BookDetailResponse> data = bookService.searchBooks(pageable, bookSearchRequest).data();

        // then
        assertThat(data).hasSize(1);
        assertThat(data.getFirst().bookCode()).isEqualTo(bookCreateRequest.bookCode());
        assertThat(data.getFirst().name()).isEqualTo(bookCreateRequest.name());
        assertThat(data.getFirst().pageCount()).isEqualTo(bookCreateRequest.pageCount());
    }

    @DisplayName("정확한 책 이름을 넣었을 때 책 검색 테스트")
    @Test
    void searchBooksByBookName() {
        // given
        bookService.addBook(bookCreateRequest);
        addRandomBooks();

        BookSearchRequest bookSearchRequest = new BookSearchRequest(false, null, bookCreateRequest.name(), null, null);
        Pageable pageable = PageRequest.of(0, 101);

        // when
        List<BookDetailResponse> data = bookService.searchBooks(pageable, bookSearchRequest).data();

        // then
        assertThat(data).hasSize(101);

    }


    @DisplayName("bookCode가 중복된 책이면 추가되지 않고 예외반환")
    @Test
    void addBook() {
        // given
        BookCreateRequest bookDuplicatedRequest = new BookCreateRequest(bookCreateRequest.bookCode(), "test_book", 200);
        bookService.addBook(bookCreateRequest);

        // when
        // then
        assertThatThrownBy(() -> bookService.addBook(bookDuplicatedRequest))
            .isInstanceOf(GeneralException.class)
            .hasMessage(BOOK_DUPLICATION.getMessage());
    }


    @DisplayName("책 정보 수정시 책이 존재하지 않으면 예외 반환")
    @Test
    void updateBook_NotFound() {
        // given
        BookUpdateRequest updateRequest = new BookUpdateRequest(1L, "1", "update_book", 200);

        // when
        // then
        assertThatThrownBy(() -> bookService.updateBook(updateRequest))
            .isInstanceOf(GeneralException.class)
            .hasMessage(BOOK_NOT_FOUND.getMessage());
    }


    private void addRandomBooks() {
        for (int i = 0; i < 100; i++) {
            String randomBookCode = RandomStringUtils.randomNumeric(13);
            int randomCount = (int) (Math.random() * 101) + 100;
            bookCreateRequest = new BookCreateRequest(randomBookCode, "test_book", randomCount);
            try {
                bookService.addBook(bookCreateRequest);
            } catch (GeneralException e) {
                System.out.println(e.getMessage());
                i--;
            }
        }
    }

}
