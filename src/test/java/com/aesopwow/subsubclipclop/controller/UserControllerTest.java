package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.user.dto.UserUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.user.service.UserService;
import com.aesopwow.subsubclipclop.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    @Order(1)
    void addStaff() throws Exception {
        // Given
        Long adminUserNo = 1L;
        String staffEmail = "staff@example.com";

        doNothing().when(userService).addStaff(adminUserNo, staffEmail);

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/user/staffs")
                        .param("adminUserNo", adminUserNo.toString())
                        .param("staffEmail", staffEmail)
        );

        // Then
        String content = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("직원이 성공적으로 추가되었습니다."))
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(2)
    void addStaff_InvalidEmailFormat() throws Exception {
        // Given
        Long adminUserNo = 1L;
        String staffEmail = "invalid-email";

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/user/staffs")
                        .param("adminUserNo", adminUserNo.toString())
                        .param("staffEmail", staffEmail)
        );

        // Then (일부러 200 OK 기대 -> 실제로는 400 Bad Request라서 실패)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(3)
    void addStaff_InternalServerError() throws Exception {
        // Given
        Long adminUserNo = 1L;
        String staffEmail = "staff@example.com";

        doThrow(new RuntimeException("서버 내부 오류가 발생했습니다."))
                .when(userService).addStaff(adminUserNo, staffEmail);

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/user/staffs")
                        .param("adminUserNo", adminUserNo.toString())
                        .param("staffEmail", staffEmail)
        );

        // Then (일부러 틀리게 - 500인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(4)
    void getStaffList() throws Exception {
        // Given
        Long adminUserNo = 1L;
        List<User> staffList = List.of(
                User.builder().userNo(2L).email("staff1@example.com").build(),
                User.builder().userNo(3L).email("staff2@example.com").build()
        );

        given(userService.getStaffList(adminUserNo)).willReturn(staffList);

        // When
        ResultActions resultActions = mockMvc.perform(
                get("/api/user/staffs")
                        .param("adminUserNo", adminUserNo.toString())
        );

        // Then
        String content = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].userNo").value(2)) // ✅ email -> userNo 검증
                .andExpect(jsonPath("$.data[1].userNo").value(3))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(5)
    void getStaffList_InvalidAdminUserNo() throws Exception {
        // Given
        String invalidAdminUserNo = "abc"; // 잘못된 입력

        // When
        ResultActions resultActions = mockMvc.perform(
                get("/api/user/staffs")
                        .param("adminUserNo", invalidAdminUserNo)
        );

        // Then (일부러 200 기대 -> 실제로는 400으로 실패 유도)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 실패 유도
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(6)
    void getStaffList_InternalServerError() throws Exception {
        // Given
        Long adminUserNo = 1L;

        doThrow(new RuntimeException("서버 내부 오류가 발생했습니다."))
                .when(userService).getStaffList(adminUserNo);

        // When
        ResultActions resultActions = mockMvc.perform(
                get("/api/user/staffs")
                        .param("adminUserNo", adminUserNo.toString())
        );

        // Then (일부러 200 기대 -> 실제로는 500으로 실패 유도)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 실패 유도
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(7)
    void updateUser() throws Exception {
        // Given
        Long userNo = 2L;
        UserUpdateRequestDTO requestDTO = UserUpdateRequestDTO.builder()
                .name("홍길동")
                .departmentName("영업팀")
                .password("newpassword123")
                .build();

        doNothing().when(userService).updateUser(userNo, requestDTO);

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/user/staffs/{userNo}", userNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
        );

        // Then
        String content = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("직원 정보가 수정되었습니다."))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(8)
    void updateUser_InvalidRequest_InvalidPathVariable() throws Exception {
        // Given
        String invalidUserNo = "invalid-string"; // 숫자가 아니라서 무조건 400

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/user/staffs/{userNo}", invalidUserNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}") // 내용은 상관없음
        );

        // Then (일부러 틀리게 - 400인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(9)
    void updateUser_InternalServerError() throws Exception {
        // Given
        Long userNo = 2L;
        UserUpdateRequestDTO requestDTO = UserUpdateRequestDTO.builder()
                .name("홍길동")
                .departmentName("영업팀")
                .password("newpassword123")
                .build();

        doThrow(new RuntimeException("서버 내부 오류가 발생했습니다."))
                .when(userService).updateUser(eq(userNo), any(UserUpdateRequestDTO.class));

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/user/staffs/{userNo}", userNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
        );

        // Then (일부러 틀리게 - 500인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(10)
    void deleteStaff() throws Exception {
        // Given
        Long userNo = 2L;

        doNothing().when(userService).deleteStaff(userNo);

        // When
        ResultActions resultActions = mockMvc.perform(
                delete("/api/user/staffs/{userNo}", userNo)
        );

        // Then
        String content = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("직원이 삭제되었습니다."))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(11)
    void deleteStaff_InvalidUserNo() throws Exception {
        // Given
        String invalidUserNo = "abc"; // 숫자 아닌 입력

        // When
        ResultActions resultActions = mockMvc.perform(
                delete("/api/user/staffs/{userNo}", invalidUserNo)
        );

        // Then (일부러 틀리게 - 400인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(12)
    void deleteStaff_InternalServerError() throws Exception {
        // Given
        Long userNo = 2L;

        doThrow(new RuntimeException("서버 내부 오류가 발생했습니다."))
                .when(userService).deleteStaff(userNo);

        // When
        ResultActions resultActions = mockMvc.perform(
                delete("/api/user/staffs/{userNo}", userNo)
        );

        // Then (일부러 틀리게 - 500인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }
}