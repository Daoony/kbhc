package com.kbhc.dto.record;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CaloriesDto {
    private String unit;
    private BigDecimal value;
}