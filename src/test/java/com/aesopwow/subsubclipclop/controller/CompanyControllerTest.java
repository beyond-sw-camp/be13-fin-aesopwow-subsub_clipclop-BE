package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.company.dto.CompanyUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.company.service.CompanyService;
import com.aesopwow.subsubclipclop.domain.info_db.repository.InfoDbRepository;
import com.aesopwow.subsubclipclop.domain.payment.repository.PaymentRepository;
import com.aesopwow.subsubclipclop.entity.Company;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyService companyService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public CompanyService companyService() {
            return Mockito.mock(CompanyService.class);
        }

        @Bean
        public PaymentRepository paymentRepository() {
            return Mockito.mock(PaymentRepository.class);
        }

        @Bean
        public InfoDbRepository infoDbRepository() {
            return Mockito.mock(InfoDbRepository.class);
        }

        @Bean
        public CompanyController companyController(
                CompanyService companyService,
                PaymentRepository paymentRepository,
                InfoDbRepository infoDbRepository
        ) throws Exception {
            CompanyController controller = new CompanyController(companyService);

            // 💡 reflection으로 private 필드 강제 주입
            java.lang.reflect.Field paymentRepoField = CompanyController.class.getDeclaredField("paymentRepository");
            paymentRepoField.setAccessible(true);
            paymentRepoField.set(controller, paymentRepository);

            java.lang.reflect.Field infoDbRepoField = CompanyController.class.getDeclaredField("infoDbRepository");
            infoDbRepoField.setAccessible(true);
            infoDbRepoField.set(controller, infoDbRepository);

            return controller;
        }
    }

    @Test
    @Order(1)
    void updateCompany() throws Exception {
        // Given
        Long companyNo = 1L;
        CompanyUpdateRequestDTO requestDTO = CompanyUpdateRequestDTO.builder()
                .companyName("새로운 회사명")
                .build();

        Company company = Company.builder()
                .companyNo(companyNo)
                .name("기존 회사명")
                .build();

        given(companyService.getCompanyByNo(companyNo)).willReturn(Optional.of(company));
        doNothing().when(companyService).save(any(Company.class));

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/company/company/{companyNo}", companyNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
        );

        // Then
        String content = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.companyNo").value(companyNo))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(2)
    void updateCompany_InvalidCompanyNoFormat() throws Exception {
        // Given
        String invalidCompanyNo = "abc";
        String request = "{\"companyName\":\"새로운 회사명\"}";

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/company/company/{companyNo}", invalidCompanyNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );

        // Then (일부러 틀리게 - 400인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(3)
    void updateCompany_CompanyNotFound() throws Exception {
        // Given
        Long companyNo = 9999L;
        CompanyUpdateRequestDTO requestDTO = CompanyUpdateRequestDTO.builder()
                .companyName("없는 회사")
                .build();

        given(companyService.getCompanyByNo(companyNo)).willReturn(Optional.empty());

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/company/company/{companyNo}", companyNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
        );

        // Then (일부러 틀리게 - 404인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }

    @Test
    @Order(4)
    void updateCompany_InternalServerError() throws Exception {
        // Given
        Long companyNo = 1L;
        CompanyUpdateRequestDTO requestDTO = CompanyUpdateRequestDTO.builder()
                .companyName("서버오류 발생")
                .build();

        given(companyService.getCompanyByNo(companyNo)).willThrow(new RuntimeException("서버 내부 오류가 발생했습니다."));

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/company/company/{companyNo}", companyNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
        );

        // Then (일부러 틀리게 - 500인데 200 기대)
        String content = resultActions
                .andExpect(status().isOk()) // ❗ 일부러 틀림
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);
    }
}