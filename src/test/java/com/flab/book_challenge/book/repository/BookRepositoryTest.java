package com.flab.book_challenge.book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.book_challenge.book.domain.Book;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    Book book1;

    @BeforeEach
    void setup() {
        String randomIsbn = RandomStringUtils.randomNumeric(13);
        int randomCount = (int) (Math.random() * 101) + 100;

        book1 = Book.builder()
            .isbn(randomIsbn)
            .name("test_book")
            .pageCount(randomCount)
            .build();

    }

    @Transactional
    @DisplayName("새로운 책 정보 추가 테스트")
    @Test
    void testSaveBook() {
        System.out.println(">>> SetUp Book1: " + book1.toString());

        // given
        String randomIsbn = RandomStringUtils.randomNumeric(13);
        int randomCount = (int) (Math.random() * 101) + 100;

        book1 = Book.builder()
            .isbn(randomIsbn)
            .name("test_book")
            .pageCount(randomCount)
            .build();

        System.out.println(">>> addNewBook Book1: " + book1.toString());

        // when
        Book saveBook1 = bookRepository.save(book1);
        System.out.println(">>> Saved saveBook1: " + saveBook1.toString());

        // then
        assertThat(saveBook1.getId()).isNotNull();
        assertThat(saveBook1.getIsbn()).isEqualTo(randomIsbn);
        assertThat(saveBook1.getPageCount()).isEqualTo(randomCount);
        assertThat(saveBook1.getName()).isEqualTo("test_book");
        assertThat(saveBook1.getId()).isGreaterThan(0);
    }

    @Transactional
    @DisplayName("책 조회 테스트")
    @Test
    void readBook() {
        // given
        System.out.println(">>> SetUp Book1: " + book1.toString());
        bookRepository.save(book1);
        System.out.println(">>> addNewBook saveBook1: " + book1.toString());

        // when
        Book saveBook1 = bookRepository.findById(book1.getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책 기본키"));

        // then
        assertThat(saveBook1.getId()).isNotNull();
        assertThat(saveBook1.getIsbn()).isEqualTo(book1.getIsbn());
        assertThat(saveBook1.getPageCount()).isEqualTo(book1.getPageCount());
        assertThat(saveBook1.getName()).isEqualTo(book1.getName());
        assertThat(saveBook1.getId()).isGreaterThan(0);
    }

    @Transactional
    @DisplayName("책 제거 테스트")
    @Test
    void removeBook() {
        // given
        System.out.println(">>> SetUp Book1: " + book1.toString());
        bookRepository.save(book1);
        System.out.println(">>> addNewBook saveBook1: " + book1.toString());

        // when
        bookRepository.delete(book1);

        // then
        assertThat(bookRepository.existsById(book1.getId())).isFalse();
    }
}