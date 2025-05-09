package com.aesopwow.subsubclipclop.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //MARK: - Not Found
    DB_INFO_NOT_FOUND("E000", "DB 정보 없음", HttpStatus.NOT_FOUND),
    ADMIN_NOT_FOUND("E001", "관리자 정보 없음", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("E002", "사용자 없음", HttpStatus.NOT_FOUND),
    ANALYSIS_NOT_FOUND("E003", "분석 정보 없음", HttpStatus.NOT_FOUND),
    COMPANY_NOT_FOUND("E004", "회사 정보 없음", HttpStatus.NOT_FOUND),
    CSV_NOT_FOUND("E005", "CSV 파일 없음", HttpStatus.NOT_FOUND),
    IMAGE_NOT_FOUND("E006", "이미지 파일 없음", HttpStatus.NOT_FOUND),
    REQUIRE_LIST_NOT_FOUND("E007", "요청 목록(RequireList)을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND("E008", "권한(Role) 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBERSHIP_NOT_FOUND("E009", "회사의 멤버십 정보가 없습니다.", HttpStatus.NOT_FOUND),
    //MARK: - Bad Request
    EMPTY_CSV("E010", "CSV 파일이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CSV_FORMAT("E011", "CSV 파일 포맷이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    STAFF_LIMIT_EXCEEDED("E012", "직원 등록 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),
    STAFF_ALREADY_EXISTS("E013", "이미 해당 직원이 추가되어 있습니다.", HttpStatus.BAD_REQUEST),
    USERNAME_REQUIRED("E014", "사용자 이름(username)은 필수 입력값입니다.", HttpStatus.BAD_REQUEST),
    //MARK: - Server Error
    HEATMAP_READ_FAILURE("E015", "히트맵 분석 데이터 읽기 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    INSIGHT_READ_FAILURE("E016", "인사이트 분석 데이터 읽기 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    BEHAVIOR_PATTERN_READ_FAILURE("E017", "행동 패턴 분석 데이터 읽기 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    ANALYSIS_API_CALL_FAILURE("E018", "API 분석 요청 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
    //MARK: - Forbidden
    ONLY_CLIENT_USER_DELETABLE("E019", "직원만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    ONLY_CLIENT_ADMIN_ALLOWED("E020", "관리자 권한이 있어야 수행할 수 있습니다.", HttpStatus.FORBIDDEN);



    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}