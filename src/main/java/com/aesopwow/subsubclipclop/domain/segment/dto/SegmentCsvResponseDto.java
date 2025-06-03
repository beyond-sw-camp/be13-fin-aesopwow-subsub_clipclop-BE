package com.aesopwow.subsubclipclop.domain.segment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SegmentCsvResponseDto {
    private boolean success;
    private List<Map<String, Object>> data;
}
