package com.aesopwow.subsubclipclop.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserUpdateRequestDTO {

    private Long userNo;

    @NotBlank(message = "사용자 이름은 필수 입력값입니다")
    @Size(min = 2, max = 20, message = "사용자 이름은 2자 이상 20자 이하여야 합니다")
    private String userName;

    @Size(max = 50, message = "부서명은 50자 이하여야 합니다")
    private String departmentName;

    private Long infoDbNo;
}