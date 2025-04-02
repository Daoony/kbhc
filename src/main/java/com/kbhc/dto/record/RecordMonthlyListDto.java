
package com.kbhc.dto.record;

import com.kbhc.constant.annotation.ExcelColumn;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RecordMonthlyListDto extends RecordExcelExportDto {

    @ExcelColumn(headerName = "Monthly")
    private final String date;

    @QueryProjection
    public RecordMonthlyListDto(
            String type,
            Integer year,
            Integer month,
            BigDecimal steps,
            BigDecimal calories,
            BigDecimal distance,
            String recordKey
    ) {
        this.steps = steps;
        this.calories = calories;
        this.distance = distance;
        this.recordKey = recordKey;

        if ("MONTHLY".equalsIgnoreCase(type)) {
            this.date = String.format("%04d-%02d", year, month);
        } else {
            this.date = "";
        }
    }
}

