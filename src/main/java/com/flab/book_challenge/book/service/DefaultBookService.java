package com.flab.book_challenge.book.service;

import static com.flab.book_challenge.common.exception.ErrorStatus.BOOK_NOT_FOUND;

import com.flab.book_challenge.book.repository.BookRepository;
import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookMapper;
import com.flab.book_challenge.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultBookService implements BookService {
    private final BookRepository bookRepository;

    @Transactional
    public void addBook(BookCreateRequest bookRequest) {

        bookRepository.findBookByIsbn(bookRequest.isbn())
            .ifPresent(book -> {
                throw new GeneralException(BOOK_NOT_FOUND);
            });

        bookRepository.save(BookMapper.toEntity(bookRequest.isbn(), bookRequest.name(), bookRequest.pageCount()));
    }
}
