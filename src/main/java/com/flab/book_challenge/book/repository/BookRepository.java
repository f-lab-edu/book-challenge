package com.flab.book_challenge.book.repository;

import com.flab.book_challenge.book.domain.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByBookCode(String bookCode);

    List<Book> findBooksByNameContaining(String name);
}
