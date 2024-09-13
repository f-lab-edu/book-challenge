package com.flab.book_challenge.book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.book_challenge.book.controller.BookSortType;
import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.service.SortCondition;
import java.util.ArrayList;
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
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    Book book1;

    @BeforeEach
    void setUp() {
        String randomBookCode = RandomStringUtils.randomNumeric(13);
        int randomCount = (int) (Math.random() * 101) + 100;

        book1 = Book.builder()
            .bookCode(randomBookCode)
            .name("test_book")
            .pageCount(randomCount)
            .build();

    }

    @DisplayName("No-Offset 방식으로 책 페이지 큰것 크기순 조회 테스트")
    @Test
    void testFindBooksNoOffset() {
        // given
        ArrayList<Book> books = new ArrayList<>(100);
        makeBooks(books);
        bookRepository.saveAll(books);

        SortCondition firstTest = SortCondition.by(false, BookSortType.PAGE_COUNT,
            "50");
        SortCondition secondTest = SortCondition.by(false, BookSortType.PAGE_COUNT,
            "10");

        // when
        List<Book> firstBooks = bookRepository.findBooksNoOffset(firstTest, 10);
        List<Book> secondBooks = bookRepository.findBooksNoOffset(secondTest, 10);

        // then
        assertThat(firstBooks).hasSize(10);
        assertThat(secondBooks).hasSize(10);
        assertThat(firstBooks.getFirst().getPageCount()).isEqualTo(49);
        assertThat(firstBooks.get(1).getPageCount()).isEqualTo(48);
        assertThat(secondBooks.getFirst().getPageCount()).isEqualTo(9);

    }

    @DisplayName("새로운 책 정보 추가 테스트")
    @Test
    void testSaveBook() {

        // given
        String randomBookCode = RandomStringUtils.randomNumeric(13);
        int randomCount = (int) (Math.random() * 101) + 100;

        book1 = Book.builder()
            .bookCode(randomBookCode)
            .name("test_book")
            .pageCount(randomCount)
            .build();

        // when
        Book saveBook1 = bookRepository.save(book1);

        // then
        assertThat(saveBook1.getId()).isNotNull();
        assertThat(saveBook1.getBookCode()).isEqualTo(randomBookCode);
        assertThat(saveBook1.getPageCount()).isEqualTo(randomCount);
        assertThat(saveBook1.getName()).isEqualTo("test_book");
        assertThat(saveBook1.getId()).isPositive();
    }

    @DisplayName("책 조회 테스트")
    @Test
    void readBook() {
        // given
        bookRepository.save(book1);

        // when
        Book saveBook1 = bookRepository.findById(book1.getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책 기본키"));

        // then
        assertThat(saveBook1.getId()).isNotNull();
        checkSameBook(saveBook1, book1);
    }

    @DisplayName("BookCode로 책 조회 테스트")
    @Test
    void readBookByBookCode() {
        // given
        ArrayList<Book> books = new ArrayList<>(100);

        makeBooks(books);

        bookRepository.saveAll(books);

        Book randomBook = books.get((int) (Math.random() * 101));

        // when
        String bookCode = randomBook.getBookCode();
        Book bookByBookCode = bookRepository.findBookByBookCode(bookCode)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 책"));

        // then
        checkSameBook(bookByBookCode, randomBook);
    }

    @DisplayName("책이름으로 책 조회 테스트")
    @Test
    void readBooksByBookName() {
        // given
        ArrayList<Book> books = new ArrayList<>(100);

        makeBooks(books);

        bookRepository.saveAll(books);

        Book randomBook = books.get((int) (Math.random() * 101));

        // when
        String bookName = randomBook.getName();
        List<Book> bookByName = bookRepository.findBooksByNameContaining(bookName);

        // then
        assertThat(bookByName).isNotEmpty();
        assertThat(bookByName.get(0).getName()).isEqualTo(randomBook.getName());
    }

    @DisplayName("일부 이름으로 책 조회 테스트")
    @Test
    void readBooksByContainingByBookName() {
        // given
        ArrayList<Book> books = new ArrayList<>(100);
        makeBooks(books);

        bookRepository.saveAll(books);
        Book randomBook = books.get((int) (Math.random() * 101));

        // when
        String bookName = randomBook.getName().substring(2, 3);
        List<Book> bookByName = bookRepository.findBooksByNameContaining(bookName);

        // then
        assertThat(bookByName).isNotEmpty();
        assertThat(bookByName.get(0).getName()).isEqualTo(randomBook.getName());
    }


    @DisplayName("책 제거 테스트")
    @Test
    void removeBook() {
        // given
        bookRepository.save(book1);

        // when
        bookRepository.delete(book1);

        // then
        assertThat(bookRepository.existsById(book1.getId())).isFalse();
    }


    @DisplayName("책 업데이트 테스트")
    @Test
    void updateBook() {
        // given
        bookRepository.save(book1);

        book1.updateBookCode("123456789");

        // when
        Book book = bookRepository.findById(book1.getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 책"));

        // then
        assertThat(book.getBookCode()).isEqualTo("123456789");


    }

    private void makeBooks(ArrayList<Book> books) {
        for (int i = 0; i < 100; i++) {
            String randomBookCode = RandomStringUtils.randomNumeric(13);

            Book book;
            book = Book.builder()
                .bookCode(randomBookCode)
                .name("test_book")
                .pageCount(i)
                .build();

            books.add(book);
        }
    }

    private void checkSameBook(Book actualBook, Book expectedBook) {

        assertThat(actualBook.getBookCode()).isEqualTo(expectedBook.getBookCode());
        assertThat(actualBook.getId()).isEqualTo(expectedBook.getId());
        assertThat(actualBook.getPageCount()).isEqualTo(expectedBook.getPageCount());
        assertThat(actualBook.getName()).isEqualTo(expectedBook.getName());
        assertThat(actualBook.getCreatedAt()).isEqualTo(expectedBook.getCreatedAt());
        assertThat(actualBook.getUpdatedAt()).isEqualTo(expectedBook.getUpdatedAt());
    }
}