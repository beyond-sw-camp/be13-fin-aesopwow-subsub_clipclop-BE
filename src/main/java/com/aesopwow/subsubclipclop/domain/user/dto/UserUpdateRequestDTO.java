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
public class UserUpdateRequestDTO {

    //MARK: - 클라이언트 관리자 정보 수정
    private String username;
    private String departmentName;
    private String password;

    //MARK: - 직원 추가
    private String staffEmail;

    //MARK: - 직원 비밀번호 변경
    private String email;
    private String oldPassword; // 임시 비밀번호
    private String newPassword;

    //MARK: - 직원 정보 수정
    private Long staffNo;
    private String staffName;

}
