package com.kbhc.dto.record;

import com.kbhc.constant.enums.SearchType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRecordListDto {

    private String fromDate;
    private String toDate;
    private String recordKey;

    @NotNull
    private SearchType searchType;

    private int pageNumber = 1;
    private int pageSize = 10;
}
