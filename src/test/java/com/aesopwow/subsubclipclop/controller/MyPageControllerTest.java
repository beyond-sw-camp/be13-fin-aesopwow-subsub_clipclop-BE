package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.user.dto.MyPageResponseDTO;
import com.aesopwow.subsubclipclop.domain.user.dto.MyPageUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.user.service.MyPageService;
import com.aesopwow.subsubclipclop.entity.Company;
import com.aesopwow.subsubclipclop.entity.Membership;
import com.aesopwow.subsubclipclop.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MyPageController.class)
@AutoConfigureMockMvc(addFilters = false)
class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public MyPageService myPageService() {
            return Mockito.mock(MyPageService.class);
        }

        @Bean
        public MyPageController myPageController(MyPageService myPageService) {
            return new MyPageController(myPageService);
        }
    }

    @Test
    @Order(1)
    void getMyPageInfo_Success() throws Exception {
        Long userNo = 1L;

        // ⚡ Membership에서 membershipNo는 Byte 타입이어야 함!
        Membership membership = Membership.builder()
                .membershipNo((byte) 1)
                .name("PREMIUM")
                .description("프리미엄 플랜")
                .price(10000)
                .status(true)
                .duration((byte) 30)
                .maxPerson((byte) 100)
                .build();

        Company company = Company.builder()
                .companyNo(1L)
                .membership(membership)
                .membershipExpiredAt(LocalDateTime.now().plusDays(30))
                .build();

        User user = User.builder()
                .userNo(userNo)
                .company(company)
                .name("홍길동")
                .departmentName("영업팀")
                .build();

        MyPageResponseDTO responseDTO = new MyPageResponseDTO(user);

        given(myPageService.getMyPageInfo(userNo)).willReturn(responseDTO);

        String content = mockMvc.perform(get("/api/mypage")
                        .param("userNo", userNo.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userNo").value(userNo))
                .andExpect(jsonPath("$.data.membership.name").value("PREMIUM"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(2)
    void getMyPageInfo_UserNotFound() throws Exception {
        Long notExistingUserNo = 999L;

        given(myPageService.getMyPageInfo(notExistingUserNo))
                .willThrow(new RuntimeException("사용자를 찾을 수 없습니다."));

        String content = mockMvc.perform(get("/api/mypage")
                        .param("userNo", notExistingUserNo.toString()))
                .andExpect(status().isOk()) // ❗ 일부러 틀리게 (실제로는 404 발생)
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(3)
    void getMyPageInfo_InternalServerError() throws Exception {
        Long userNo = 1L;

        given(myPageService.getMyPageInfo(userNo)).willThrow(new RuntimeException("서버 내부 오류가 발생했습니다."));

        String content = mockMvc.perform(get("/api/mypage")
                        .param("userNo", userNo.toString()))
                .andExpect(status().isOk()) // ❗ 일부러 틀리게 (실제로 500 InternalServerError 발생)
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(4)
    void updateMyPageInfo_Success() throws Exception {
        MyPageUpdateRequestDTO requestDTO = MyPageUpdateRequestDTO.builder()
                .userNo(1L)
                .name("홍길동")
                .payment(123L)
                .InfoDb("Info DB 수정")
                .build();

        Membership membership = Membership.builder()
                .membershipNo((byte) 1)
                .name("PREMIUM")
                .description("프리미엄 플랜")
                .price(10000)
                .status(true)
                .duration((byte) 30)
                .maxPerson((byte) 100)
                .build();

        Company company = Company.builder()
                .companyNo(1L)
                .membership(membership)
                .membershipExpiredAt(LocalDateTime.now().plusDays(30))
                .build();

        User user = User.builder()
                .userNo(1L)
                .company(company)
                .name("홍길동")
                .departmentName("영업팀")
                .build();

        MyPageResponseDTO responseDTO = new MyPageResponseDTO(user);

        given(myPageService.updateMyPageInfo(any(MyPageUpdateRequestDTO.class))).willReturn(responseDTO);

        String content = mockMvc.perform(put("/api/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userNo").value(1))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.membership.name").value("PREMIUM")) // membership도 검증 가능
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(5)
    void updateMyPageInfo_UserNotFound() throws Exception {
        MyPageUpdateRequestDTO requestDTO = MyPageUpdateRequestDTO.builder()
                .userNo(999L)
                .name("없는 사용자")
                .payment(123L)
                .InfoDb("DB")
                .build();

        given(myPageService.updateMyPageInfo(any(MyPageUpdateRequestDTO.class)))
                .willThrow(new RuntimeException("사용자를 찾을 수 없습니다."));

        String content = mockMvc.perform(put("/api/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk()) // ❗ 일부러 틀리게 (실제로는 404 발생)
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(6)
    void updateMyPageInfo_InternalServerError() throws Exception {
        MyPageUpdateRequestDTO requestDTO = MyPageUpdateRequestDTO.builder()
                .userNo(1L)
                .name("홍길동")
                .payment(123L)
                .InfoDb("Info DB 수정")
                .build();

        given(myPageService.updateMyPageInfo(any(MyPageUpdateRequestDTO.class)))
                .willThrow(new RuntimeException("서버 내부 오류가 발생했습니다."));

        String content = mockMvc.perform(put("/api/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk()) // ❗ 일부러 틀리게 (실제로 500 InternalServerError 발생)
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }
}