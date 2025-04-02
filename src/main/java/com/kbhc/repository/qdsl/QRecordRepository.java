package com.kbhc.repository.qdsl;

import com.kbhc.constant.enums.SearchType;
import com.kbhc.constant.utils.UtilCommon;
import com.kbhc.dto.record.*;
import com.kbhc.entity.QCustomerRecord;
import com.kbhc.entity.QRecordEntry;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@RequiredArgsConstructor
public class QRecordRepository {

    private final JPAQueryFactory queryFactory;

    QCustomerRecord customerRecord = QCustomerRecord.customerRecord;
    QRecordEntry recordEntry = QRecordEntry.recordEntry;

    public Page<RecordListDto> findRecordList(SearchRecordListDto dto, Pageable pageable) {
        SearchType type = dto.getSearchType();

        List<RecordListDto> results = queryFactory
                .select(getProjection(type))
                .from(recordEntry)
                .join(recordEntry.customerRecord, customerRecord)
                .where(
                        regDateBetween(dto.getFromDate(), dto.getToDate()),
                        recordkeyEq(dto.getRecordKey())
                )
                .groupBy(getGroupByExpressions(type))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(customerRecord.count())
                .from(recordEntry)
                .join(recordEntry.customerRecord, customerRecord)
                .where(
                        regDateBetween(dto.getFromDate(), dto.getToDate()),
                        recordkeyEq(dto.getRecordKey())
                )
                .groupBy(getGroupByExpressions(type))
                .fetch()
                .size();

        return new PageImpl<>(results, pageable, total);
    }

    public List<RecordDailyListDto> findDailyRecords() {
        return queryFactory
                .select(getDailyExportProjection())
                .from(recordEntry)
                .join(recordEntry.customerRecord, customerRecord)
                .groupBy(
                        recordEntry.startPeriod.year(),
                        recordEntry.startPeriod.month(),
                        recordEntry.startPeriod.dayOfMonth(),
                        customerRecord.recordKey
                )
                .fetch();
    }

    public List<RecordMonthlyListDto> findMonthlyRecords() {
        return queryFactory
                .select(getMonthlyExportProjection())
                .from(recordEntry)
                .join(recordEntry.customerRecord, customerRecord)
                .groupBy(
                        recordEntry.startPeriod.year(),
                        recordEntry.startPeriod.month(),
                        customerRecord.recordKey
                )
                .fetch();
    }

    private ConstructorExpression<RecordListDto> getProjection(SearchType type) {
        if (type == SearchType.DAILY) {
            return new QRecordListDto(
                    constant("DAILY"),
                    recordEntry.startPeriod.year(),
                    recordEntry.startPeriod.month(),
                    recordEntry.startPeriod.dayOfMonth(),
                    recordEntry.steps.sum(),
                    recordEntry.caloriesValue.sum(),
                    recordEntry.distanceValue.sum(),
                    customerRecord.recordKey
            );
        } else {
            return new QRecordListDto(
                    constant("MONTHLY"),
                    recordEntry.startPeriod.year(),
                    recordEntry.startPeriod.month(),
                    constant(0),
                    recordEntry.steps.sum(),
                    recordEntry.caloriesValue.sum(),
                    recordEntry.distanceValue.sum(),
                    customerRecord.recordKey
            );
        }
    }

    private Expression<?>[] getGroupByExpressions(SearchType type) {
        if (type == SearchType.DAILY) {
            return new Expression<?>[]{
                    recordEntry.startPeriod.year(),
                    recordEntry.startPeriod.month(),
                    recordEntry.startPeriod.dayOfMonth(),
                    customerRecord.recordKey
            };
        } else {
            return new Expression<?>[]{
                    recordEntry.startPeriod.year(),
                    recordEntry.startPeriod.month(),
                    customerRecord.recordKey
            };
        }
    }

    private ConstructorExpression<RecordDailyListDto> getDailyExportProjection() {
        return new QRecordDailyListDto(
                constant("DAILY"),
                recordEntry.startPeriod.year(),
                recordEntry.startPeriod.month(),
                recordEntry.startPeriod.dayOfMonth(),
                recordEntry.steps.sum(),
                recordEntry.caloriesValue.sum(),
                recordEntry.distanceValue.sum(),
                customerRecord.recordKey
        );
    }

    private ConstructorExpression<RecordMonthlyListDto> getMonthlyExportProjection() {
        return new QRecordMonthlyListDto(
                constant("MONTHLY"),
                recordEntry.startPeriod.year(),
                recordEntry.startPeriod.month(),
                recordEntry.steps.sum(),
                recordEntry.caloriesValue.sum(),
                recordEntry.distanceValue.sum(),
                customerRecord.recordKey
        );
    }

    private BooleanExpression regDateBetween(String startRegDate, String endRegDate) {
        Map<String, LocalDateTime> map = UtilCommon.getLocalDateTimeForSearch(startRegDate, endRegDate);
        return map.isEmpty() ? null : recordEntry.startPeriod.between(map.get("startDateTime"), map.get("endDateTime"));
    }

    private BooleanExpression recordkeyEq(String recordkey) {
        return recordkey != null ? customerRecord.recordKey.eq(recordkey) : null;
    }
}
