package com.flab.book_challenge.book.controller;

import static com.flab.book_challenge.common.exception.ErrorStatus.QUERY_NOT_FOUND;

import ch.qos.logback.core.util.StringUtil;
import com.flab.book_challenge.book.request.BookCreateRequest;
import com.flab.book_challenge.book.request.BookUpdateRequest;
import com.flab.book_challenge.book.response.BookDetailResponse;
import com.flab.book_challenge.book.service.BookService;
import com.flab.book_challenge.common.exception.GeneralException;
import com.flab.book_challenge.common.header.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book", description = "책 관리 API")
@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "책 전체 조회", tags = "Book")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getBooks() {
        List<BookDetailResponse> books = bookService.getBooks();

        return ResponseEntity.ok(new ApiResponse<>(books));
    }

    @Operation(summary = "책 검색", tags = "Book")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchBooks(
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) String name) {

        if (StringUtil.notNullNorEmpty(isbn)) {
            return ResponseEntity.ok(new ApiResponse<>(findBookByIsbn(isbn)));
        } else if (StringUtil.notNullNorEmpty(name)) {
            return ResponseEntity.ok(new ApiResponse<>(findBooksByName(name)));
        }

        throw new GeneralException(QUERY_NOT_FOUND);
    }

    @Operation(summary = "책 추가", tags = "Book")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addBook(
        @RequestBody @Valid BookCreateRequest request) {

        long id = bookService.addBook(request);
        return ResponseEntity.ok(new ApiResponse<>(id));
    }

    @Operation(summary = "책 수정", tags = "Book")
    @PutMapping
    public ResponseEntity<ApiResponse<?>> updateBook(
        @RequestBody @Valid BookUpdateRequest request) {

        long id = bookService.updateBook(request);
        return ResponseEntity.ok(new ApiResponse<>(id));
    }

    @Operation(summary = "책 삭제", tags = "Book")
    @DeleteMapping
    public ResponseEntity<?> deleteBook(
        @RequestParam long id) {

        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    private BookDetailResponse findBookByIsbn(String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    private List<BookDetailResponse> findBooksByName(String name) {
        return bookService.getBooksByName(name);
    }

}
