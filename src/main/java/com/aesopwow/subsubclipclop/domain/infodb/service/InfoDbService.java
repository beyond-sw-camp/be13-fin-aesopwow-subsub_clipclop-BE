package com.aesopwow.subsubclipclop.domain.infodb.service;

import com.aesopwow.subsubclipclop.domain.infodb.dto.InfoDbResponseDto;

public interface InfoDbService {
    InfoDbResponseDto getInfoDb(Long infoDbNo);
}
