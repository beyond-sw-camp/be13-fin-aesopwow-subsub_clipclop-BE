package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.common.dto.BaseResponseDto;
import com.aesopwow.subsubclipclop.domain.user.dto.MyPageResponseDTO;
import com.aesopwow.subsubclipclop.domain.user.dto.MyPageUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.user.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
@Tag(name = "MyPage APIs", description = "마이페이지 관련 API 목록")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("")
    @Operation(summary = "마이페이지 유저 정보 조회", description = "현재 로그인한 유저의 마이페이지 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "User Not Found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<BaseResponseDto<MyPageResponseDTO>> getMyPageInfo(@RequestParam Long userNo) {
        MyPageResponseDTO myPageInfo = myPageService.getMyPageInfo(userNo);

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, myPageInfo));
    }

    @PutMapping
    public ResponseEntity<BaseResponseDto<MyPageResponseDTO>> updateMyPageInfo(
            @Valid @RequestBody MyPageUpdateRequestDTO myPageUpdateRequestDTO) {

        MyPageResponseDTO updatedInfo = myPageService.updateMyPageInfo(myPageUpdateRequestDTO);

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, updatedInfo));
    }
}
