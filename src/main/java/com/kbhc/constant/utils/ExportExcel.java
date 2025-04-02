package com.kbhc.constant.utils;

import com.kbhc.constant.Constants;
import com.kbhc.constant.annotation.ExcelColumn;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ExportExcel<T> {

    private final SXSSFWorkbook workbook;
    private final Sheet sheet;
    private int currentRowIndex = 0;

    public ExportExcel(String sheetName) {
        this.workbook = new SXSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
    }

    public <E extends T> void writeSection(String sectionTitle, List<E> dataList, Class<E> typeClass) {
        if (dataList == null || dataList.isEmpty()) return;

        Row titleRow = sheet.createRow(currentRowIndex++);
        titleRow.createCell(0).setCellValue(sectionTitle);

        renderHeaders(typeClass);
        renderExcel(dataList, typeClass);
    }

    private <E> void renderHeaders(Class<E> typeClass) {
        Row row = sheet.createRow(currentRowIndex++);
        int columnIndex = 0;
        CellStyle cellStyle = setCellStyle(Constants.HEADER);

        // 모든 상위 클래스 필드도 포함하는 경우:
        for (Field field : getAllFields(typeClass)) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);
                Cell cell = row.createCell(columnIndex++);
                cell.setCellValue(columnAnnotation.headerName());
                cell.setCellStyle(cellStyle);
            }
        }
    }

    private <E> void renderExcel(List<E> dataList, Class<E> typeClass) {
        CellStyle cellStyle = setCellStyle(Constants.BODY);

        for (E data : dataList) {
            Row row = sheet.createRow(currentRowIndex++);
            renderBody(data, row, typeClass, cellStyle);
        }
    }


    private <E> void renderBody(E data, Row row, Class<E> typeClass, CellStyle cellStyle) {
        int cellIndex = 0;
        for (Field field : getAllFields(typeClass)) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(data);
                    Cell cell = row.createCell(cellIndex++);
                    // 직접 cell.setCellValue 대신 setCellValue 메서드를 호출합니다.
                    setCellValue(cell, value);
                    cell.setCellStyle(cellStyle);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else if (value instanceof Boolean bool) {
            cell.setCellValue(bool);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private CellStyle setCellStyle(String type) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        if (Objects.equals(type, Constants.HEADER)) {
            Font font = workbook.createFont();
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        return cellStyle;
    }

    public void write(HttpServletResponse response, String fileName) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + ".xlsx\"; filename*=UTF-8''" + encodedFileName + ".xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            workbook.dispose();
        }
    }
}