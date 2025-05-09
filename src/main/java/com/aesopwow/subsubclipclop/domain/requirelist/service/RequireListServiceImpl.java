package com.aesopwow.subsubclipclop.domain.requirelist.service;

import com.aesopwow.subsubclipclop.domain.analysis.repository.AnalysisRepository;
import com.aesopwow.subsubclipclop.domain.company.repository.CompanyRepository;
import com.aesopwow.subsubclipclop.domain.infodb.repository.InfoDbRepository;
import com.aesopwow.subsubclipclop.domain.requirelist.dto.RequireListRequestDto;
import com.aesopwow.subsubclipclop.domain.requirelist.dto.RequireListResponseDto;
import com.aesopwow.subsubclipclop.domain.requirelist.repository.RequireListRepository;
import com.aesopwow.subsubclipclop.entity.Analysis;
import com.aesopwow.subsubclipclop.entity.Company;
import com.aesopwow.subsubclipclop.entity.DbInfo;
import com.aesopwow.subsubclipclop.entity.RequireList;
import com.aesopwow.subsubclipclop.global.enums.ErrorCode;
import com.aesopwow.subsubclipclop.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequireListServiceImpl implements RequireListService {
    private final RequireListRepository requireListRepository;
    private final CompanyRepository companyRepository;
    private final AnalysisRepository analysisRepository;
    private final InfoDbRepository infoDbRepository;

    @Override
    @Transactional(readOnly = true)
    public RequireListResponseDto getRequireList(Long requireListNo) {
        RequireList requireList = requireListRepository.findById(requireListNo)
                .orElseThrow(() -> new CustomException(ErrorCode.REQUIRE_LIST_NOT_FOUND));

        return new RequireListResponseDto(requireList);
    }

    @Override
    @Transactional
    public RequireListResponseDto createRequireList(RequireListRequestDto requireListRequestDto) {
        Analysis analysis = analysisRepository.findById(requireListRequestDto.getAnalysisNo())
                .orElseThrow(() -> new CustomException(ErrorCode.ANALYSIS_NOT_FOUND));

        Company company = companyRepository.findById(requireListRequestDto.getCompanyNo())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

        DbInfo dbInfo = infoDbRepository.findById(requireListRequestDto.getDbInfoNo())
                .orElseThrow(() -> new CustomException(ErrorCode.DB_INFO_NOT_FOUND));

        RequireList requireList = RequireList.builder()
                .analysis(analysis)
                .company(company)
                .dbInfo(dbInfo)
                .build();

        requireListRepository.save(requireList);

        return new RequireListResponseDto(requireList);
    }
}