package com.aesopwow.subsubclipclop.domain.api.service;

import com.aesopwow.subsubclipclop.domain.api.dto.ApiRequestDto;
import com.aesopwow.subsubclipclop.domain.api.dto.ApiResponseDto;
import com.aesopwow.subsubclipclop.domain.info_column.dto.InfoColumnResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {
    private final WebClient webClient = WebClient.create("http://127.0.0.1:5000");

    @Override
    public String callExternalApi(Long companyNo) {
        return webClient.get()
                .uri("/python-api/info_db")
                .attribute("company_no", companyNo)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 방식으로 결과 대기
    }

    @Override
    public List<InfoColumnResponseDto> callExternalApiInfoColumns(Long infoDbNo, String originTable) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/python-api/info_column")
                        .queryParam("info_db_no", infoDbNo)
                        .queryParam("origin_table", originTable)
                        .build()
                )
                .retrieve()
                .bodyToFlux(InfoColumnResponseDto.class) // JSON 배열 -> List
                .collectList()
                .block(); // 동기 방식으로 결과 대기
    }

    @Override
    public byte[] getAnalysisResult(String infoDbNo) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/python-api/analysis")
                        .queryParam("infoDbNo", infoDbNo)
                        .build()
                )
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block(); // 동기 방식으로 대기
    }

    @Override
    public ApiResponseDto requestAnalysis(ApiRequestDto apiRequestDto) {
        return webClient.post()
                .uri("/python-api/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiRequestDto)
                .retrieve()
                .bodyToMono(ApiResponseDto.class)
                .block();
    }

}
