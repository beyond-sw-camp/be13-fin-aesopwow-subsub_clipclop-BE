package com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CohortDoubleAnalysisRemainHeatmapResponseDto {
    private String firstContent;
    private List<String> firstColumnLabels; // ["USERS", "DAY 0", ...]
    private List<List<String>> firstDataRows; // [["2024-04-01", "10989", "100.0", ...], ...]

    private String secondContent;
    private List<String> secondColumnLabels; // ["USERS", "DAY 0", ...]
    private List<List<String>> secondDataRows; // [["2024-04-01", "11234", "100.0", ...], ...]
}