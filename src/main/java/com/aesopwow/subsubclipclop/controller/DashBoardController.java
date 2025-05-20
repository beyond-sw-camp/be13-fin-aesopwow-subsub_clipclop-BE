package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.api.dto.ApiResponseDto;
import com.aesopwow.subsubclipclop.domain.api.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dash-board")
@RequiredArgsConstructor
public class DashBoardController {
    private final ApiService apiService;

//    @ExceptionHandler
    @GetMapping("/{infoDbNo}")
    public ResponseEntity<ApiResponseDto> getDashBoardCSV(
            @PathVariable(required = true) String infoDbNo) {

        byte[] statCardCSV = apiService.getAnalysisResult(infoDbNo);
        ApiResponseDto apiResponseDto = new ApiResponseDto(statCardCSV);

        return ResponseEntity.ok(apiResponseDto);
    }
}
