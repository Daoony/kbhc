package com.kbhc.dto.record;

import lombok.Data;
import java.util.List;

@Data
    public class RecordDataDto {
        private List<RecordEntryDto> entries;
        private String memo;
}

