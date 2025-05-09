package com.aesopwow.subsubclipclop.domain.infodb.service;

import com.aesopwow.subsubclipclop.domain.infodb.dto.InfoDbResponseDto;
import com.aesopwow.subsubclipclop.domain.infodb.repository.InfoDbRepository;
import com.aesopwow.subsubclipclop.global.enums.ErrorCode;
import com.aesopwow.subsubclipclop.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoDbServiceImpl implements InfoDbService {
    private final InfoDbRepository infoDbRepository;

    @Override
    public InfoDbResponseDto getInfoDb(Long infoDbId) {
        InfoDb infoDb = infoDbRepository.findById(infoDbId)
                .orElseThrow(() -> new CustomException(ErrorCode.DB_INFO_NOT_FOUND));

        return InfoDbResponseDto.builder()
                .dbInfoNo(infoDb.getInfoDbNo())
                .companyNo(infoDb.getCompany().getCompanyNo())
                .name(infoDb.getName())
                .createdAt(infoDb.getCreatedAt())
                .updatedAt(infoDb.getUpdatedAt())
                .build();
    }
}
