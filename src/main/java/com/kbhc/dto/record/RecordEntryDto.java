package com.kbhc.dto.record;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordEntryDto {
    private PeriodDto period;
    private DistanceDto distance;
    private CaloriesDto calories;
    private BigDecimal steps;
}