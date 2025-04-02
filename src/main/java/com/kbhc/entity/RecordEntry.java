package com.kbhc.entity;

import com.kbhc.constant.enums.Unit;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "record_entry")
@Getter
@NoArgsConstructor
public class RecordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;

    @Enumerated(EnumType.STRING)
    private Unit distanceUnit;

    @Column(precision = 10, scale = 2)
    private BigDecimal distanceValue;

    @Enumerated(EnumType.STRING)
    private Unit caloriesUnit;

    @Column(precision = 10, scale = 2)
    private BigDecimal caloriesValue;

    @Column(precision = 10, scale = 2)
    private BigDecimal steps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_record_id")
    private CustomerRecord record;

    public void updateRecord(CustomerRecord record) {
        this.record = record;
    }

    @Builder
    public RecordEntry(LocalDateTime startPeriod, LocalDateTime endPeriod, Unit distanceUnit, BigDecimal distanceValue, Unit caloriesUnit, BigDecimal caloriesValue, BigDecimal steps) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.distanceUnit = distanceUnit;
        this.distanceValue = distanceValue;
        this.caloriesUnit = caloriesUnit;
        this.caloriesValue = caloriesValue;
        this.steps = steps;
    }
}
