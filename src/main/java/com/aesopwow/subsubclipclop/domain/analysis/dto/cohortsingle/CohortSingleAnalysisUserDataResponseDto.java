package com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CohortSingleAnalysisUserDataResponseDto {
    private List<Map<String, String>> tableData; // 각 유저 행 = 필드-값 map
}