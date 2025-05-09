package com.aesopwow.subsubclipclop.domain.user.service;


import com.aesopwow.subsubclipclop.domain.company.dto.CompanyUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.company.repository.CompanyRepository;
import com.aesopwow.subsubclipclop.domain.membership.repository.MembershipRepository;
import com.aesopwow.subsubclipclop.domain.user.dto.MyPageResponseDTO;
import com.aesopwow.subsubclipclop.domain.user.dto.MyPageUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.user.repository.UserRepository;
import com.aesopwow.subsubclipclop.entity.Company;
import com.aesopwow.subsubclipclop.entity.Membership;
import com.aesopwow.subsubclipclop.entity.User;
import com.aesopwow.subsubclipclop.global.enums.ErrorCode;
import com.aesopwow.subsubclipclop.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;

    @Override
    public MyPageResponseDTO getMyPageInfo(Long userNo) {
        User user = userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Company company = user.getCompany();
        if (company == null) {
            throw new CustomException(ErrorCode.COMPANY_NOT_FOUND);
        }

        Membership membership = company.getMembership();
        if (membership == null) {
            throw new CustomException(ErrorCode.ONLY_CLIENT_USER_DELETABLE);
        }

        return MyPageResponseDTO.builder()
                .userNo(user.getUserNo())
                .username(user.getUsername())
                .membershipName(membership.getName())
                .build();
    }

    @Override
    public MyPageResponseDTO updateMyPageInfo(MyPageUpdateRequestDTO myPageUpdateRequestDTO) {
        User user = userRepository.findByUserNo(myPageUpdateRequestDTO.getUserNo())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (myPageUpdateRequestDTO.getUsername() != null) {
            user.setUsername(myPageUpdateRequestDTO.getUsername());
        }

        userRepository.save(user);

        return MyPageResponseDTO.builder()
                .userNo(user.getUserNo())
                .username(user.getUsername())
                .membershipName("미지정")
                .build();
    }

    @Override
    public void updateCompanyInfo(CompanyUpdateRequestDTO companyUpdateRequestDTO) {
        // TODO: 회사 정보 업데이트 구현
    }
}
