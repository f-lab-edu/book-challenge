package com.flab.book_challenge.book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.book_challenge.book.domain.Book;
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
@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    Book book1;

    @BeforeEach
    void setUp() {
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
        System.out.println(">>> Saved saveBook1: " + saveBook1);

        // then
        assertThat(saveBook1.getId()).isNotNull();
        assertThat(saveBook1.getIsbn()).isEqualTo(randomIsbn);
        assertThat(saveBook1.getPageCount()).isEqualTo(randomCount);
        assertThat(saveBook1.getName()).isEqualTo("test_book");
        assertThat(saveBook1.getId()).isPositive();
    }

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
        checkSameBook(saveBook1, book1);
    }

    @DisplayName("ISBN으로 책 조회 테스트")
    @Test
    void readBookByIsbn() {
        // given
        ArrayList<Book> books = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            String randomIsbn = RandomStringUtils.randomNumeric(13);
            int randomCount = (int) (Math.random() * 101) + 100;
            Book book;
            book = Book.builder()
                .isbn(randomIsbn)
                .name("test_book")
                .pageCount(randomCount)
                .build();

            books.add(book);
        }

        bookRepository.saveAll(books);

        Book randomBook = books.get((int) (Math.random() * 101));
        System.out.println(">>> randomBook: " + randomBook.toString());

        // when
        String isbn = randomBook.getIsbn();
        Book bookByIsbn = bookRepository.findBookByIsbn(isbn)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 책"));
        System.out.println(">>> bookByIsbn: " + bookByIsbn.toString());

        // then
        checkSameBook(bookByIsbn, randomBook);
    }

    @DisplayName("책이름으로 책 조회 테스트")
    @Test
    void readBooksByBookName() {
        // given
        ArrayList<Book> books = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            String randomIsbn = RandomStringUtils.randomNumeric(13);
            int randomCount = (int) (Math.random() * 101) + 100;
            Book book;
            book = Book.builder()
                .isbn(randomIsbn)
                .name("test_book")
                .pageCount(randomCount)
                .build();

            books.add(book);
        }

        bookRepository.saveAll(books);

        Book randomBook = books.get((int) (Math.random() * 101));
        System.out.println(">>> randomBook: " + randomBook.toString());

        // when
        String bookName = randomBook.getName();
        List<Book> bookByName = bookRepository.findBookByName(bookName);
        System.out.println(">>> bookByBookName: " + bookByName.toString());

        // then
        assertThat(bookByName).isNotEmpty();
        assertThat(bookByName.get(0).getName()).isEqualTo(randomBook.getName());
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

    @Transactional
    @DisplayName("책 업데이트 테스트")
    @Test
    void updateBook() {
        // given
        bookRepository.save(book1);
        System.out.println(">>> addNewBook saveBook1: " + book1.toString());

        Book updatedBook = book1.update(Book.builder()
            .isbn("123456789")
            .name("test_book")
            .pageCount(100)
            .build());

        System.out.println(">>> updateBook: " + updatedBook.toString());

        // when
        Book book = bookRepository.findById(book1.getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 책"));

        System.out.println(">>> updatedAfterBook: " + book);

        // then
        checkSameBook(book, updatedBook);


    }

    private void checkSameBook(Book actualBook, Book expectedBook) {

        assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
        assertThat(actualBook.getId()).isEqualTo(expectedBook.getId());
        assertThat(actualBook.getPageCount()).isEqualTo(expectedBook.getPageCount());
        assertThat(actualBook.getName()).isEqualTo(expectedBook.getName());
    }
}