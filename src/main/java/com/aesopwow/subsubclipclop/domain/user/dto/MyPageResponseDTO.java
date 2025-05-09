package com.aesopwow.subsubclipclop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResponseDTO {
    private final Long userNo;
    private final String membershipName;
    private LocalDateTime membershipExpiredAt;
    private final String username;
    private final String departmentName;
}