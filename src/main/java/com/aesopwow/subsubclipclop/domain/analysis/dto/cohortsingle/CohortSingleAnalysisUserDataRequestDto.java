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
public class CohortSingleAnalysisUserDataRequestDto {
    private String clusterType;
    private List<String> fields;
}