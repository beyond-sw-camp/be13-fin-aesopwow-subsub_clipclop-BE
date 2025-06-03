package com.aesopwow.subsubclipclop.domain.segment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SegmentFileResponseDto {
    private byte[] content;   // 파일 바이너리 데이터
    private String filename;  // 파일명
}
