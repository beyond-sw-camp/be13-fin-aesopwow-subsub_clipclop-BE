package com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CohortSingleAnalysisRemainHeatmapResponseDto {
    private String content;
    private List<String> columnLabels; // ["USERS", "DAY 0", "DAY 1", ...]
    private List<List<String>> dataRows; // [["2024-04-01", "10989", "100.0", ...], ...]
}