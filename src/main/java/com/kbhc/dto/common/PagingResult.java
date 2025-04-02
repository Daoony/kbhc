package com.kbhc.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingResult {

    private Long totalCount;
    private int totalPages;
    private int currentPage;
    private Object list;
}