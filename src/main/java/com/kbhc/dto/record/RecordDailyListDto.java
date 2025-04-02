package com.kbhc.dto.record;

import com.kbhc.constant.annotation.ExcelColumn;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RecordDailyListDto extends RecordExcelExportDto {

    @ExcelColumn(headerName = "Daily")
    private final String date;

    @QueryProjection
    public RecordDailyListDto(
            String type,
            Integer year,
            Integer month,
            Integer day,
            BigDecimal steps,
            BigDecimal calories,
            BigDecimal distance,
            String recordKey
    ) {

        this.steps = steps;
        this.calories = calories;
        this.distance = distance;
        this.recordKey = recordKey;

        if ("DAILY".equals(type) && day != null && day != 0) {
            this.date = String.format("%04d-%02d-%02d", year, month, day);
        } else {
            this.date = "";
        }
    }
}
