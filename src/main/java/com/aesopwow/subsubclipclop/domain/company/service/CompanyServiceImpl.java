package com.aesopwow.subsubclipclop.domain.company.service;

import com.aesopwow.subsubclipclop.entity.Company;
import com.aesopwow.subsubclipclop.entity.InfoDb;
import com.aesopwow.subsubclipclop.entity.Payment;
import com.aesopwow.subsubclipclop.domain.company.dto.CompanyUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.company.repository.CompanyRepository;
import com.aesopwow.subsubclipclop.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;
    private PaymentRepository paymentRepository;
    private InfoDbRepository infoDbRepository;

    @Override
    public void updateCompanyInfo(Long companyNo, CompanyUpdateRequestDTO companyUpdateRequestDTO) {
        Company company = companyRepository.findById(companyNo)
                .orElseThrow(() -> new RuntimeException("회사 정보를 찾을 수 없습니다."));

        if (companyUpdateRequestDTO.getCompanyName() != null) {
            company.setName(companyUpdateRequestDTO.getCompanyName());
        }
//        if (companyUpdateRequestDTO.getDepartmentName() != null) {
//            company.setDepartmentName(companyUpdateRequestDTO.getDepartmentName());
//        }

        if (companyUpdateRequestDTO.getPayment() != null) {
            Payment payment = paymentRepository.findById(companyUpdateRequestDTO.getPayment())
                    .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));
            saveCompanyPayment(company, payment);
        }

        if (companyUpdateRequestDTO.getInfoDb() != null) {
            InfoDb infoDb = infoDbRepository.findById(Long.valueOf(companyUpdateRequestDTO.getInfoDb()))
                    .orElseThrow(() -> new RuntimeException("DB 정보를 찾을 수 없습니다."));
            saveCompanyDb(company, infoDb);
        }

        companyRepository.save(company);
    }

    @Override
    public Optional<Company> getCompanyByNo(Long companyNo) {
        return Optional.empty();
    }

    @Override
    public void save(Company company) {

    }

    @Override
    public void saveCompanyPayment(Company companyNo, Payment payment) {
        payment.setCompany(companyNo);
        paymentRepository.save(payment);
    }

    @Override
    public void saveCompanyDb(Company companyNo, InfoDb infoDb) {
        infoDb.setCompany(companyNo);
        infoDbRepository.save(infoDb);

    }
}
