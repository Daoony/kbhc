package com.kbhc.dto.record;

import com.kbhc.constant.annotation.ExcelColumn;
import java.math.BigDecimal;

public abstract class RecordExcelExportDto {

    @ExcelColumn(headerName = "Steps")
    protected BigDecimal steps;

    @ExcelColumn(headerName = "calories")
    protected BigDecimal calories;

    @ExcelColumn(headerName = "distance")
    protected BigDecimal distance;

    @ExcelColumn(headerName = "recordKey")
    protected String recordKey;

}
