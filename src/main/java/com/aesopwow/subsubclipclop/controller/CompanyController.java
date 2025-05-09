package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.infodb.repository.InfoDbRepository;
import com.aesopwow.subsubclipclop.entity.Company;
import com.aesopwow.subsubclipclop.entity.InfoDb;
import com.aesopwow.subsubclipclop.entity.Payment;
import com.aesopwow.subsubclipclop.domain.common.dto.BaseResponseDto;
import com.aesopwow.subsubclipclop.domain.company.dto.CompanyUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.company.service.CompanyService;
import com.aesopwow.subsubclipclop.domain.payment.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/company")
@Tag(name = "Company", description = "회사 관련 API")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private PaymentRepository paymentRepository;
    private InfoDbRepository infoDbRepository;

    @PutMapping("/company/{companyNo}")
    @Operation(summary = "회사 정보 수정", description = "회사 정보를 JSON으로 받아 수정한다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD REQUEST",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL SERVER ERROR",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })

    public ResponseEntity<BaseResponseDto<Company>> updateCompany(
            @Parameter(description = "회사 번호", example = "1")
            @PathVariable("companyNo") Long companyNo,
            @Valid @RequestBody CompanyUpdateRequestDTO companyUpdateRequestDTO) {

        Company company = companyService.getCompanyByNo(companyNo)
                .orElseThrow(() -> new RuntimeException("회사를 찾을 수 없습니다."));

        if (companyUpdateRequestDTO.getCompanyName() != null) company.setName(companyUpdateRequestDTO.getCompanyName());
//        if (companyUpdateRequestDTO.getDepartmentName() != null) company.setDepartmentName(companyUpdateRequestDTO.getDepartmentName());

        if (companyUpdateRequestDTO.getPayment() != null) {
            Payment payment = paymentRepository.findById(companyUpdateRequestDTO.getPayment())
                    .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));
            companyService.saveCompanyPayment(company, payment);
        }


        if (companyUpdateRequestDTO.getInfoDb() != null) {
            InfoDb infoDb = infoDbRepository.findById(Long.valueOf(companyUpdateRequestDTO.getInfoDb()))
                    .orElseThrow(() -> new RuntimeException("DB 정보를 찾을 수 없습니다."));
            companyService.saveCompanyDb(company, infoDb);
        }

        companyService.save(company);

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, company));
    }
}