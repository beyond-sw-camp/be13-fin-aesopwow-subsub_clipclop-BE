package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.api.dto.ApiRequestDto;
import com.aesopwow.subsubclipclop.domain.api.service.ApiService;
import com.aesopwow.subsubclipclop.domain.require_list.dto.RequireListRequestDto;
import com.aesopwow.subsubclipclop.domain.require_list.dto.RequireListResponseDto;
import com.aesopwow.subsubclipclop.domain.require_list.service.RequireListService;
import com.aesopwow.subsubclipclop.global.enums.ErrorCode;
import com.aesopwow.subsubclipclop.global.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequireListController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RequireListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequireListService requireListService;

    @Autowired
    private ApiService apiService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RequireListService requireListService() {
            return Mockito.mock(RequireListService.class);
        }

        @Bean
        public ApiService apiService() {
            return Mockito.mock(ApiService.class);
        }
    }

    // ---------------- getRequireList ----------------
    @Test @Order(1)
    void getRequireList_200() throws Exception {
        given(requireListService.getRequireList(1L)).willReturn(new RequireListResponseDto());

        String content = mockMvc.perform(get("/api/request-list/{requireListNo}", 1L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test @Order(2)
    void getRequireList_400() throws Exception {
        String invalidRequireListNo = "abc";

        String content = mockMvc.perform(get("/api/request-list/{requireListNo}", invalidRequireListNo))
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test @Order(3)
    void getRequireList_500() throws Exception {
        given(requireListService.getRequireList(1L)).willThrow(new RuntimeException("서버 내부 오류"));

        String content = mockMvc.perform(get("/api/request-list/{requireListNo}", 1L))
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    // ---------------- createRequireList ----------------
    @Test @Order(4)
    void createRequireList_200() throws Exception {
        RequireListRequestDto requestDto = new RequireListRequestDto(1L, 2L, 3L);
        given(requireListService.createRequireList(any())).willReturn(new RequireListResponseDto());

        String content = mockMvc.perform(post("/api/request-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test @Order(5)
    void createRequireList_500() throws Exception {
        RequireListRequestDto requestDto = new RequireListRequestDto(1L, 2L, 3L);
        given(requireListService.createRequireList(any())).willReturn(new RequireListResponseDto());
        willThrow(new CustomException(ErrorCode.ANALYSIS_API_CALL_FAILURE))
                .given(apiService).requestAnalysis(any(ApiRequestDto.class));

        String content = mockMvc.perform(post("/api/request-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }
}