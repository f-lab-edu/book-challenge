package com.flab.book_challenge.book.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BookDetailResponse(
    long id,
    String bookCode,
    String name,
    int pageCount,
    LocalDateTime createAt
) {
}
