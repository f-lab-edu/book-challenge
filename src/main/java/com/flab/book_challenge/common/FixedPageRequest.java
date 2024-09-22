package com.flab.book_challenge.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class FixedPageRequest extends PageRequest {

    public FixedPageRequest(Pageable pageable, long totalCount) {
        super(getPageNo(pageable, totalCount), pageable.getPageSize(), pageable.getSort());

    }

    private static int getPageNo(Pageable pageable, long totalCount) {
        int pageNo = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long requestCount = (long) (pageNo - 1) * pageSize; // pageNo:11, pageSize:10 일 경우 requestCount=100

        if (totalCount > requestCount) { // 실제 건수가 요청한 페이지 번호보다 높을 경우
            return pageNo;
        }

        // 실제 건수가 부족한 경우 요청 페이지 번호를 가장 높은 번호로 교체 100/11= 9.x = 10
        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
