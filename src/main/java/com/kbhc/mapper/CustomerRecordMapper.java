package com.kbhc.mapper;

import com.kbhc.constant.enums.Unit;
import com.kbhc.dto.record.RegisterRecordDto;
import com.kbhc.entity.CustomerRecord;
import com.kbhc.entity.RecordEntry;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Mapper(componentModel = "spring")
public class CustomerRecordMapper {

    private static final DateTimeFormatter[] FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    };

    public static CustomerRecord toEntity(RegisterRecordDto dto) {
        CustomerRecord customerRecord = CustomerRecord.builder()
                .recordKey(dto.getRecordkey())
                .memo(dto.getData().getMemo())
                .build();

        List<RecordEntry> entries = dto.getData().getEntries().stream()
                .map(entryDto -> {
                    LocalDateTime start = parseDate(entryDto.getPeriod().getFrom());
                    LocalDateTime end = parseDate(entryDto.getPeriod().getTo());

                    RecordEntry entry = RecordEntry.builder()
                            .startPeriod(start)
                            .endPeriod(end)
                            .distanceUnit(Unit.valueOf(entryDto.getDistance().getUnit().toUpperCase()))
                            .distanceValue(entryDto.getDistance().getValue())
                            .caloriesUnit(Unit.valueOf(entryDto.getCalories().getUnit().toUpperCase()))
                            .caloriesValue(entryDto.getCalories().getValue())
                            .steps(entryDto.getSteps())
                            .build();

                    entry.updateRecord(customerRecord);
                    return entry;
                }).toList();

        customerRecord.getEntries().addAll(entries);
        return customerRecord;
    }

    private static LocalDateTime parseDate(String dateStr) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new IllegalArgumentException("Unparsable date: " + dateStr);
    }

    private static int parseSteps(Object stepsObj) {
        try {
            return (int) Double.parseDouble(stepsObj.toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
