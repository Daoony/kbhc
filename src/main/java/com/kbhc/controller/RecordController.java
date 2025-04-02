package com.kbhc.controller;

import com.kbhc.constant.utils.UtilCommon;
import com.kbhc.dto.common.ApiResult;
import com.kbhc.dto.common.PagingResult;
import com.kbhc.dto.record.*;
import com.kbhc.service.RecordService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("records")
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    public ResponseEntity<ApiResult> searchRecordList(@Valid SearchRecordListDto dto) {
        ApiResult apiResult = new ApiResult();

        PageRequest pageRequest = PageRequest.of(dto.getPageNumber() - 1, dto.getPageSize());
        Page<RecordListDto> resultPage = recordService.searchList(dto, pageRequest);

        PagingResult pagingResult = UtilCommon.makePagingResult(resultPage);
        apiResult.setData(pagingResult);

        return ResponseEntity.ok().body(apiResult);
    }

    @PostMapping
    public ResponseEntity<ApiResult> uploadRecords(@RequestPart("file") MultipartFile file)  {
        ApiResult apiResult = new ApiResult();

        RecordIdDto recordIdDto = recordService.registerRecordList(file);
        apiResult.setData(recordIdDto);

        return ResponseEntity.ok().body(apiResult);
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<RecordDailyListDto> dailyList = recordService.getDailyRecords();
        List<RecordMonthlyListDto> monthlyList = recordService.getMonthlyRecords();

        recordService.exportRecordExcel(response, dailyList, monthlyList);
    }
}
