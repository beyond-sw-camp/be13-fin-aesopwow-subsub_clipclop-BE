package com.aesopwow.subsubclipclop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageUpdateRequestDTO {
    private Long userNo;
    private String username;
    private Long payment;
    private String InfoDb;
}
