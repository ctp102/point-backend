package com.point.core.common.utils;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class Paging {

    private int pageNo;         // 현재 페이지 번호
    private int pageSize;       // 현재 페이지에서 조회할 수 있는 데이터 수
    private long totalElements; // 전체 데이터 수(totalCount)
    private long totalPage;     // 전체 페이지 수
    private Sort sort;          // 정렬 정보

    public Pageable toPageable() {
        return Pageable
                .ofSize(pageSize)
                .withPage(pageNo >= 1 ? pageNo - 1 : 0);
    }

    @Builder
    private Paging(int pageNo, int pageSize, long totalElements, long totalPage, Sort sort) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
        this.sort = sort;
    }


    public static Paging of(Page<?> page) {
        return builder()
                .pageNo(page.getPageable().getPageNumber() + 1)
                .pageSize(page.getPageable().getPageSize())
                .totalElements(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .sort(page.getPageable().getSort())
                .build();
    }

}

