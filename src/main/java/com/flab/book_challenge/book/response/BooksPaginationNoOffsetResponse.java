package com.flab.book_challenge.book.response;

import java.util.List;
import lombok.Builder;

@Builder
public record BooksPaginationNoOffsetResponse(
    List<BookDetailResponse> data,
    String next
) {
}
