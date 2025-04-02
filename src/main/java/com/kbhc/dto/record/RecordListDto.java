package com.kbhc.dto.record;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RecordListDto {
    private final String type;
    private final String date;
    private final BigDecimal steps;
    private final BigDecimal calories;
    private final BigDecimal distance;
    private final String recordKey;

    @QueryProjection
    public RecordListDto(
            String type,
            Integer year,
            Integer month,
            Integer day,
            BigDecimal steps,
            BigDecimal calories,
            BigDecimal distance,
            String recordKey
    ) {
        this.type = type;
        if (day != null && day != 0) {
            this.date = String.format("%04d-%02d-%02d", year, month, day);
        } else {
            this.date = String.format("%04d-%02d", year, month);
        }
        this.steps = steps;
        this.calories = calories;
        this.distance = distance;
        this.recordKey = recordKey;
    }
}


