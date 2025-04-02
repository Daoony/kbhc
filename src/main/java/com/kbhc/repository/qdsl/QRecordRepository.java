package com.kbhc.repository.qdsl;

import com.kbhc.constant.enums.SearchType;
import com.kbhc.dto.record.*;
import com.kbhc.entity.QCustomerRecord;
import com.kbhc.entity.QRecordEntry;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@RequiredArgsConstructor
public class QRecordRepository {

    private final JPAQueryFactory queryFactory;

    QCustomerRecord record = QCustomerRecord.customerRecord;
    QRecordEntry entry = QRecordEntry.recordEntry;

    public Page<RecordListDto> findRecordList(SearchRecordListDto dto, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();

        BooleanExpression dateExpr = betweenDate(dto.getFromDate(), dto.getToDate());
        BooleanExpression keyExpr = recordKeyEq(dto.getRecordKey());

        if (dateExpr != null) where.and(dateExpr);
        if (keyExpr != null) where.and(keyExpr);

        List<RecordListDto> results;

        if (dto.getSearchType() == SearchType.DAILY) {
            results = queryFactory
                    .select(new QRecordListDto(
                            constant("DAILY"),
                            entry.startPeriod.year(),
                            entry.startPeriod.month(),
                            entry.startPeriod.dayOfMonth(),
                            entry.steps.sum(),
                            entry.caloriesValue.sum(),
                            entry.distanceValue.sum(),
                            record.recordKey
                    ))
                    .from(entry)
                    .join(entry.record, record)
                    .where(where)
                    .groupBy(
                            entry.startPeriod.year(),
                            entry.startPeriod.month(),
                            entry.startPeriod.dayOfMonth(),
                            record.recordKey
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            results = queryFactory
                    .select(new QRecordListDto(
                            constant("MONTHLY"),
                            entry.startPeriod.year(),
                            entry.startPeriod.month(),
                            constant(0), // day = 0 for monthly
                            entry.steps.sum(),
                            entry.caloriesValue.sum(),
                            entry.distanceValue.sum(),
                            record.recordKey
                    ))
                    .from(entry)
                    .join(entry.record, record)
                    .where(where)
                    .groupBy(
                            entry.startPeriod.year(),
                            entry.startPeriod.month(),
                            record.recordKey
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        long total;
        if (dto.getSearchType() == SearchType.DAILY) {
            total = queryFactory
                    .select(record.recordKey.countDistinct())
                    .from(entry)
                    .join(entry.record, record)
                    .where(where)
                    .groupBy(
                            entry.startPeriod.year(),
                            entry.startPeriod.month(),
                            entry.startPeriod.dayOfMonth(),
                            record.recordKey
                    )
                    .fetch()
                    .size();
        } else {
            total = queryFactory
                    .select(record.recordKey.countDistinct())
                    .from(entry)
                    .join(entry.record, record)
                    .where(where)
                    .groupBy(
                            entry.startPeriod.year(),
                            entry.startPeriod.month(),
                            record.recordKey
                    )
                    .fetch()
                    .size();
        }

        return new PageImpl<>(results, pageable, Optional.of(total).orElse(0L));
    }

    public List<RecordDailyListDto> findDailyRecords() {
        BooleanBuilder where = new BooleanBuilder();

        return queryFactory
                .select(new QRecordDailyListDto(
                        constant("DAILY"),
                        entry.startPeriod.year(),
                        entry.startPeriod.month(),
                        entry.startPeriod.dayOfMonth(),
                        entry.steps.sum(),
                        entry.caloriesValue.sum(),
                        entry.distanceValue.sum(),
                        record.recordKey
                ))
                .from(entry)
                .join(entry.record, record)
                .where(where)
                .groupBy(
                        entry.startPeriod.year(),
                        entry.startPeriod.month(),
                        entry.startPeriod.dayOfMonth(),
                        record.recordKey
                )
                .fetch();
    }

    public List<RecordMonthlyListDto> findMonthlyRecords() {
        BooleanBuilder where = new BooleanBuilder();

        return queryFactory
                .select(new QRecordMonthlyListDto(
                        constant("MONTHLY"),
                        entry.startPeriod.year(),
                        entry.startPeriod.month(),
                        entry.steps.sum(),
                        entry.caloriesValue.sum(),
                        entry.distanceValue.sum(),
                        record.recordKey
                ))
                .from(entry)
                .join(entry.record, record)
                .where(where)
                .groupBy(
                        entry.startPeriod.year(),
                        entry.startPeriod.month(),
                        record.recordKey
                )
                .fetch();
    }

    private BooleanExpression betweenDate(String from, String to) {
        if (from == null || to == null) return null;
        return entry.startPeriod.between(LocalDateTime.parse(from), LocalDateTime.parse(to));
    }

    private BooleanExpression recordKeyEq(String recordKey) {
        return recordKey != null ? record.recordKey.eq(recordKey) : null;
    }
}
