package com.aesopwow.subsubclipclop.domain.segment.service;

import com.aesopwow.subsubclipclop.domain.segment.dto.SegmentCsvResponseDto;
import com.aesopwow.subsubclipclop.domain.segment.dto.SegmentFileListResponseDto;
import com.aesopwow.subsubclipclop.domain.segment.dto.SegmentSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Map;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SegmentService {

    private static final String PYTHON_SERVER_URL = "http://127.0.0.1:5001/api/segment/download";
    private static final String PYTHON_LIST_URL = "http://127.0.0.1:5001/api/segment/list";
    private static final String PYTHON_GET_CSV_URL = "http://127.0.0.1:5001/api/segment/list/";

    /**
     * 세그먼트 CSV 파일 생성 및 S3 저장 요청
     */
    public SegmentSaveResponseDto getSegmentCsv(
            int infoDbNo,
            String userInfo,
            String userSubInfo,
            String targetColumn
    ) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PYTHON_SERVER_URL)
                .queryParam("info_db_no", infoDbNo)
                .queryParam("user_info", userInfo)
                .queryParam("user_sub_info", userSubInfo)
                .queryParam("target_column", targetColumn);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<SegmentSaveResponseDto> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                SegmentSaveResponseDto.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("CSV 저장 실패: " + response.getStatusCode());
        }
    }

    /**
     * S3에 저장된 세그먼트 CSV 파일 목록 조회
     */
    public SegmentFileListResponseDto getSegmentFileList(
            int infoDbNo,
            String targetColumn
    ) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PYTHON_LIST_URL)
                .queryParam("info_db_no", infoDbNo)
                .queryParam("target_column", targetColumn);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<SegmentFileListResponseDto> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                SegmentFileListResponseDto.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("S3 파일 목록 조회 실패: " + response.getStatusCode());
        }
    }
    public Object getSegmentCsvFile(String s3Key) {
        RestTemplate restTemplate = new RestTemplate();

        // Flask API 호출 URL 생성
        String url = PYTHON_GET_CSV_URL + s3Key;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                byte[].class
        );
        byte[] csvBytes = response.getBody();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("CSV 파일 조회 실패: " + response.getStatusCode());
        }
    }
}
