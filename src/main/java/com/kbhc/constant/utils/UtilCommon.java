package com.kbhc.constant.utils;

import com.kbhc.constant.Constants;
import com.kbhc.dto.common.PagingResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, LocalDateTime> getLocalDateTimeForSearch(String startDate, String endDate) {
        Map<String, LocalDateTime> localDateTimeMap = new HashMap<>();

        if (StringUtils.isNotBlank(startDate)) {
            localDateTimeMap.put("startDateTime", getLocalDate(startDate).atStartOfDay());
        }

        if (StringUtils.isNotBlank(endDate)) {
            localDateTimeMap.put("endDateTime", getLocalDate(endDate).atTime(23, 59, 59, 999999999));
        }

        return localDateTimeMap;
    }

    public static LocalDate getLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(Constants.YYYY_MM_DD));
    }

}