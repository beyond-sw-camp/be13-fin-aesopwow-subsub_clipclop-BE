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
public class CohortDoubleAnalysisUserDataRequestDto {
    private String firstClusterType;
    private String secondClusterType;
    private List<String> fields;
}