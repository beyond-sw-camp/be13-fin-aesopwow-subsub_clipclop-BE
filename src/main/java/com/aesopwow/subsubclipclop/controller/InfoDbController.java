package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.api.service.ApiService;
import com.aesopwow.subsubclipclop.domain.infodb.dto.InfoDbResponseDto;
import com.aesopwow.subsubclipclop.domain.infodb.service.InfoDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dbInfo")
@RequiredArgsConstructor
public class InfoDbController {
    private final InfoDbService infoDbService;
    private final ApiService apiService;

    @GetMapping("/{infoDbNo}")
    public ResponseEntity<InfoDbResponseDto> getInfoDb(
            @PathVariable Long infoDbNo) {
        InfoDbResponseDto infoDbResponseDto = infoDbService.getInfoDb(infoDbNo);

        return ResponseEntity.ok(infoDbResponseDto);
    }

    @GetMapping("/ext/{company_no}")
    public ResponseEntity<String> getExternalInfo_db(
            @PathVariable Long company_no) {
        String str = apiService.callExternalApi(company_no);

        return ResponseEntity.ok(str);
    }

//    @GetMapping("")
//    public ResponseEntity<Info_dbResponseDto> getInfo_dbs(
//            @RequestParam Info_dbRequestDto requestDto) {
//        List<Info_dbResponseDto> info_dbResponseDto = info_dbService.getInfo_dbs(requestDto.getInfo_db_no());
//
//        return ResponseEntity.ok(info_dbResponseDto);
//    }
}
