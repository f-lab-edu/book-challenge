package com.flab.book_challenge.book.service;

import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_DUPLICATION;
import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.common.exception.GeneralException;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        String randomIsbn = RandomStringUtils.randomNumeric(13);
        int randomCount = (int) (Math.random() * 101) + 100;

        bookCreateRequest = new BookCreateRequest(randomIsbn, "test_book", randomCount);

    }


    @DisplayName("책 전체 조회")
    @Test
    void getBooks() {
        // given
        addRandomBooks();
        // when
        List<BookDetailResponse> books = bookService.getBooks();
        // then
        int randomIndex = (int) (Math.random() * 100);

        assertThat(books).hasSize(100);
        assertThat(books.get(randomIndex).id()).isNotZero();
        assertThat(books.get(randomIndex).isbn()).isNotNull();
        assertThat(books.get(randomIndex).name()).isNotNull();
        assertThat(books.get(randomIndex).pageCount()).isNotZero();
    }


    @DisplayName("책 ISBN으로 조회")
    @Test
    void getBookByIsbn() {
        // given
        bookService.addBook(bookCreateRequest);
        // when
        BookDetailResponse book = bookService.getBookByIsbn(bookCreateRequest.isbn());
        // then
        assertThat(book.id()).isNotZero();
        assertThat(book.isbn()).isEqualTo(bookCreateRequest.isbn());
        assertThat(book.name()).isEqualTo(bookCreateRequest.name());
        assertThat(book.pageCount()).isEqualTo(bookCreateRequest.pageCount());
    }

    @DisplayName("존재하지 않는 책 ISBN으로 조회시 예외 반환")
    @Test
    void getBookByIsbn_NotFound() {
        // given
        // when
        // then
        assertThatThrownBy(() -> bookService.getBookByIsbn("1"))
            .isInstanceOf(GeneralException.class)
            .hasMessage(BOOK_NOT_FOUND.getMessage());
    }

    @DisplayName("책 이름으로 조회")
    @Test
    void getBookByName() {
        // given
        addRandomBooks();

        // when
        List<BookDetailResponse> books = bookService.getBooksByName("test_book");

        // then
        int randomIndex = (int) (Math.random() * 100);

        assertThat(books).hasSize(100);
        assertThat(books.get(randomIndex).id()).isNotZero();
        assertThat(books.get(randomIndex).isbn()).isNotNull();
        assertThat(books.get(randomIndex).name()).isEqualTo("test_book");
        assertThat(books.get(randomIndex).pageCount()).isNotZero();
    }


    @DisplayName("isbn이 중복된 책이면 추가되지 않고 예외반환")
    @Test
    void addBook() {
        // given
        BookCreateRequest bookDuplicatedRequest = new BookCreateRequest(bookCreateRequest.isbn(), "test_book", 200);
        bookService.addBook(bookCreateRequest);

        // when
        // then
        assertThatThrownBy(() -> bookService.addBook(bookDuplicatedRequest))
            .isInstanceOf(GeneralException.class)
            .hasMessage(BOOK_DUPLICATION.getMessage());
    }

    @DisplayName("책 정보 수정")
    @Test
    void updateBook() {
        // given
        long saveBookId = bookService.addBook(bookCreateRequest);
        BookUpdateRequest updateRequest = new BookUpdateRequest(saveBookId, bookCreateRequest.isbn(), "update_book",
            200);

        // when
        bookService.updateBook(updateRequest);

        // then
        BookDetailResponse book = bookService.getBookByIsbn(bookCreateRequest.isbn());
        assertThat(book.id()).isNotZero();
        assertThat(book.isbn()).isEqualTo(bookCreateRequest.isbn());
        assertThat(book.name()).isEqualTo("update_book");
        assertThat(book.pageCount()).isEqualTo(200);
    }

    @DisplayName("책 정보 수정시 책이 존재하지 않으면 예외 반환")
    @Test
    void updateBook_NotFound() {
        // given
        BookUpdateRequest updateRequest = new BookUpdateRequest(1, "1", "update_book", 200);

        // when
        // then
        assertThatThrownBy(() -> bookService.updateBook(updateRequest))
            .isInstanceOf(GeneralException.class)
            .hasMessage(BOOK_NOT_FOUND.getMessage());
    }

    @DisplayName("책 삭제 테스트")
    @Test
    void deleteBook() {
        // given
        long saveBookId = bookService.addBook(bookCreateRequest);

        // when
        bookService.deleteBook(saveBookId);

        // then
        String isbn = bookCreateRequest.isbn();
        
        assertThatThrownBy(() -> bookService.getBookByIsbn(isbn))
            .isInstanceOf(GeneralException.class)
            .hasMessage(BOOK_NOT_FOUND.getMessage());

    }

    private void addRandomBooks() {
        for (int i = 0; i < 100; i++) {
            String randomIsbn = RandomStringUtils.randomNumeric(13);
            int randomCount = (int) (Math.random() * 101) + 100;
            bookCreateRequest = new BookCreateRequest(randomIsbn, "test_book", randomCount);
            try {
                bookService.addBook(bookCreateRequest);
            } catch (GeneralException e) {
                System.out.println(e.getMessage());
                i--;
            }
        }
    }

}
