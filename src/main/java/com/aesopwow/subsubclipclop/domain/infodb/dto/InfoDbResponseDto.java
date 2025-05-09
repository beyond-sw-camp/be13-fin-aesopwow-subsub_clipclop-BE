package com.aesopwow.subsubclipclop.domain.infodb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoDbResponseDto {
    private Long dbInfoNo;
    private Long companyNo;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
