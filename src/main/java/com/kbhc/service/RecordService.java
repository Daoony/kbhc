package com.kbhc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbhc.constant.enums.ErrorCode;
import com.kbhc.constant.enums.SearchType;
import com.kbhc.constant.utils.ExportExcel;
import com.kbhc.dto.record.*;
import com.kbhc.entity.CustomerRecord;
import com.kbhc.exception.ExceptionNotFound;
import com.kbhc.mapper.CustomerRecordMapper;
import com.kbhc.repository.RecordRepository;
import com.kbhc.repository.qdsl.QRecordRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final QRecordRepository qRecordRepository;
    private final ObjectMapper objectMapper;

    public Page<RecordListDto> searchList(SearchRecordListDto dto, Pageable pageable) {
        return qRecordRepository.findRecordList(dto, pageable);
    }

    public RecordIdDto registerRecord(RegisterRecordDto dto) {
        return saveRecord(dto);
    }

    public RecordIdDto registerRecordList(MultipartFile file) {
        try {
            RegisterRecordDto dto = objectMapper.readValue(file.getInputStream(), RegisterRecordDto.class);
            return saveRecord(dto);
        } catch (IOException e) {
            throw new ExceptionNotFound(ErrorCode.FILE_NOT_FOUND);
        }
    }

    public void exportRecordExcel(HttpServletResponse response,
                                  List<RecordDailyListDto> dailyList,
                                  List<RecordMonthlyListDto> monthlyList) throws IOException {
        ExportExcel<RecordExcelExportDto> exporter = new ExportExcel<>("Records");

        exporter.writeSection("<Daily>", dailyList, RecordDailyListDto.class);
        exporter.writeSection("<Monthly>", monthlyList, RecordMonthlyListDto.class);

        exporter.write(response, "record_data");
    }


    public List<RecordDailyListDto> getDailyRecords() {
        return qRecordRepository.findDailyRecords();
    }

    public List<RecordMonthlyListDto> getMonthlyRecords() {
        return qRecordRepository.findMonthlyRecords();
    }

    private RecordIdDto saveRecord(RegisterRecordDto dto) {
        CustomerRecord record = CustomerRecordMapper.toEntity(dto);
        CustomerRecord saved = recordRepository.save(record);
        return new RecordIdDto(saved.getId());
    }
}
