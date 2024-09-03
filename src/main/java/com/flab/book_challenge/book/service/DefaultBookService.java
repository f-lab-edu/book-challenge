package com.flab.book_challenge.book.service;

import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_DUPLICATION;
import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_NOT_FOUND;

import com.flab.book_challenge.book.BookMapper;
import com.flab.book_challenge.book.domain.Book;
import com.flab.book_challenge.book.repository.BookRepository;
import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.common.exception.GeneralException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultBookService implements BookService {
    private final BookRepository bookRepository;


    @Override
    public List<BookDetailResponse> getBooks() {
        return bookRepository.findAll().stream()
            .map(book -> BookMapper.toResponse(book.getId(), book.getBookCode(), book.getName(), book.getPageCount()))
            .toList();
    }

    // bookCode 조회는 단일 책을, 이름 검색은 책 목록을 반환합니다.

    @Override
    public BookDetailResponse getBookByBookCode(String bookCode) {
        return bookRepository.findBookByBookCode(bookCode)
            .map(book -> BookMapper.toResponse(book.getId(), book.getBookCode(), book.getName(), book.getPageCount()))
            .orElseThrow(() -> new GeneralException(BOOK_NOT_FOUND));
    }

    @Override
    public List<BookDetailResponse> getBooksByName(String name) {
        return bookRepository.findBooksByName(name).stream()
            .map(book -> BookMapper.toResponse(book.getId(), book.getBookCode(), book.getName(), book.getPageCount()))
            .toList();
    }


    @Transactional
    @Override
    public long addBook(BookCreateRequest bookRequest) {

        existsBookByBookCode(bookRequest.bookCode());

        Book saveBook = bookRepository.save(
            BookMapper.toEntity(bookRequest.bookCode(), bookRequest.name(), bookRequest.pageCount()));

        return saveBook.getId();
    }

    @Transactional
    @Override
    public long updateBook(BookUpdateRequest updateRequest) {

        Book book = existsBookById(updateRequest.id());

        if (!book.getBookCode().equals(updateRequest.bookCode())) {
            existsBookByBookCode(updateRequest.bookCode());
        }

        Book update = book.update(
            BookMapper.toEntity(updateRequest.bookCode(), updateRequest.name(), updateRequest.pageCount()));

        return update.getId();

    }

    @Transactional
    @Override
    public void deleteBook(long bookId) {
        Book book = existsBookById(bookId);
        bookRepository.delete(book);
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

}
