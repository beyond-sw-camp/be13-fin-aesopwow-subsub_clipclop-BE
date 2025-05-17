package com.aesopwow.subsubclipclop.domain.analysis.service;

import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisInsightRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisInsightResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisRemainHeatmapRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisRemainHeatmapResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisUserDataRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisUserDataResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisVisualizationRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisVisualizationResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisInsightRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisInsightResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisRemainHeatmapRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisRemainHeatmapResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisUserDataRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisUserDataResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisVisualizationRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisVisualizationResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.repository.RequestListJpaRepository;
import com.aesopwow.subsubclipclop.domain.analysis.repository.AnalysisRepository;
import com.aesopwow.subsubclipclop.entity.Analysis;
import com.aesopwow.subsubclipclop.entity.Company;
import com.aesopwow.subsubclipclop.entity.InfoDb;
import com.aesopwow.subsubclipclop.entity.RequireList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisRepository repository;
    private final RequestListJpaRepository jpaRepository;

    // 임시로 지정한 것. 나중에 삭제할 것.
    private static final Long FIXED_COMPANY_NO = 1L;
    private static final Byte SINGLE_ANALYSIS_NO = 1;
    private static final Byte DOUBLE_ANALYSIS_NO = 2;
    private static final Long FIXED_DB_NO = 1L;

    @Override
    public CohortSingleAnalysisInsightResponseDto fetchSingleInsight(CohortSingleAnalysisInsightRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(SINGLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchSingleInsight(requestDto.getClusterType());
    }

    @Override
    public CohortSingleAnalysisRemainHeatmapResponseDto fetchSingleRemainHeatmap(CohortSingleAnalysisRemainHeatmapRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(SINGLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchSingleRemainHeatmap(requestDto);
    }

    @Override
    public CohortSingleAnalysisVisualizationResponseDto fetchSingleVisualization(CohortSingleAnalysisVisualizationRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(SINGLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchSingleVisualization(requestDto.getClusterType());
    }

    @Override
    public CohortSingleAnalysisUserDataResponseDto fetchSingleUserData(CohortSingleAnalysisUserDataRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(SINGLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchSingleUserData(requestDto);
    }

    @Override
    public CohortDoubleAnalysisInsightResponseDto fetchDoubleInsight(CohortDoubleAnalysisInsightRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(DOUBLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchDoubleInsight(requestDto.getFirstClusterType(), requestDto.getSecondClusterType());
    }

    @Override
    public CohortDoubleAnalysisRemainHeatmapResponseDto fetchDoubleRemainHeatmap(CohortDoubleAnalysisRemainHeatmapRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(DOUBLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchDoubleRemainHeatmap(requestDto);
    }

    @Override
    public CohortDoubleAnalysisVisualizationResponseDto fetchDoubleVisualization(CohortDoubleAnalysisVisualizationRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(DOUBLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchDoubleVisualization(requestDto);
    }

    @Override
    public CohortDoubleAnalysisUserDataResponseDto fetchDoubleUserData(CohortDoubleAnalysisUserDataRequestDto requestDto) {
        jpaRepository.save(RequireList.builder()
                .analysis(Analysis.builder().analysisNo(DOUBLE_ANALYSIS_NO).build())
                .company(Company.builder().companyNo(FIXED_COMPANY_NO).build())
                .infoDb(InfoDb.builder().infoDbNo(FIXED_DB_NO).build())
                .build());
        return repository.fetchDoubleUserData(requestDto);
    }
}