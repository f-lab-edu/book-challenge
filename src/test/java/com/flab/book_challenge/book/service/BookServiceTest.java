package com.flab.book_challenge.book.service;

import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_NOT_FOUND;

import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.common.exception.GeneralException;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
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

    @Transactional
    @DisplayName("isbn이 중복된 책이면 추가되지 않고 예외반환")
    @Test
    void addBook() {
        // given
        BookCreateRequest bookDuplicatedRequest = new BookCreateRequest(bookCreateRequest.isbn(), "test_book", 200);
        bookService.addBook(bookCreateRequest);

        // when
        // then
        Assertions.assertThatThrownBy(() -> bookService.addBook(bookDuplicatedRequest))
            .isInstanceOf(GeneralException.class)
            .hasMessage(BOOK_NOT_FOUND.getMessage());
    }


}
