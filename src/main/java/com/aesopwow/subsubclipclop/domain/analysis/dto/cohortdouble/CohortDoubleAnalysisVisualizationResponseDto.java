package com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CohortDoubleAnalysisVisualizationResponseDto {
    private String firstImageBase64A;
    private String firstImageBase64B;
    private String secondImageBase64A;
    private String secondImageBase64B;
}