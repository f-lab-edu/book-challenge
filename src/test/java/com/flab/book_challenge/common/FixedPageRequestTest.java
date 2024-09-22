package com.flab.book_challenge.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class FixedPageRequestTest {

    @ParameterizedTest
    @CsvSource({
        // pageNo, pageSize, totalCount, expectedPageNo
        "5, 10, 100, 5",
        "9, 10, 100, 9",
        "10, 10, 100, 10",
        "11, 10, 100, 10",
        "12,10,100,10",
        "1, 10, 5, 1"
    })
    void testFixedPageRequest(int pageNo, int pageSize, long totalCount, int expectedPageNo) {
        // Given
        Pageable originalPageable = PageRequest.of(pageNo, pageSize, Sort.unsorted());

        // When
        FixedPageRequest fixedPageRequest = new FixedPageRequest(originalPageable, totalCount);

        // Then
        assertEquals(expectedPageNo, fixedPageRequest.getPageNumber());
        assertEquals(pageSize, fixedPageRequest.getPageSize());
    }

}