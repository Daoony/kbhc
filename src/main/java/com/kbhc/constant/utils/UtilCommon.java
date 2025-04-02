package com.kbhc.constant.utils;

import com.kbhc.dto.common.PagingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Slf4j
public class UtilCommon {

    private UtilCommon() {
        // 이 클래스는 정적(static) 메서드만 제공합니다.
        // get, find, make 등 인자 값을 받아 가공하여 결과를 제공하는 함수입니다.
    }

    public static <T> PagingResult makePagingResult(Page<T> page) {
        PagingResult pagingResult = new PagingResult();

        pagingResult.setTotalCount(page.getTotalElements());
        pagingResult.setTotalPages(page.getTotalPages());
        pagingResult.setCurrentPage(page.getNumber() + 1);
        pagingResult.setList(page.getContent());

        return pagingResult;
    }

}