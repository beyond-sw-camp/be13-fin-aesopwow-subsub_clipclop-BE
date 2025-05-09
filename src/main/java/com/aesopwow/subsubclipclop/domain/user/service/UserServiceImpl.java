package com.aesopwow.subsubclipclop.domain.user.service;

import com.aesopwow.subsubclipclop.domain.role.repository.RoleRepository;
import com.aesopwow.subsubclipclop.domain.user.dto.UserUpdateRequestDTO;
import com.aesopwow.subsubclipclop.domain.user.repository.UserRepository;
import com.aesopwow.subsubclipclop.entity.Role;
import com.aesopwow.subsubclipclop.entity.User;
import com.aesopwow.subsubclipclop.global.enums.ErrorCode;
import com.aesopwow.subsubclipclop.global.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void updateUser(Long userNo, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (userUpdateRequestDTO.getUsername() != null) {
            user.setUsername(userUpdateRequestDTO.getUsername());
        } else {
            throw new CustomException(ErrorCode.USERNAME_REQUIRED);
        }

        userRepository.save(user);
    }

    public void addStaff(Long adminUserNo, String staffEmail) {
        User admin = userRepository.findByUserNo(adminUserNo)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        User staff = userRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (staff.getCompany() != null &&
                staff.getCompany().equals(admin.getCompany()) &&
                staff.getRole().getName() == Role.RoleType.CLIENT_USER
        ) {
            throw new CustomException(ErrorCode.STAFF_ALREADY_EXISTS);
        }

        staff.setCompany(admin.getCompany());
        staff.setRole(roleRepository.findByName(Role.RoleType.CLIENT_USER)
                .orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND)));

        int maxStaff = admin.getPlanType().getMaxStaff();

        int currentCount = userRepository.countByCompanyAndRole_Name(
                admin.getCompany(),
                Role.RoleType.CLIENT_USER
        );

        if (currentCount >= maxStaff) {
            throw new CustomException(ErrorCode.STAFF_LIMIT_EXCEEDED);
        }

        userRepository.save(staff);
    }

    public List<User> getStaffList(Long adminUserNo) {
        User admin = userRepository.findByUserNo(adminUserNo)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (admin.getRole().getName() != Role.RoleType.CLIENT_ADMIN) {
            throw new CustomException(ErrorCode.ONLY_CLIENT_ADMIN_ALLOWED);
        }

        return userRepository.findByCompanyAndRole_NameAndIsDeletedFalse(
                admin.getCompany(),
                Role.RoleType.CLIENT_USER
        );
    }

    public void deleteStaff(Long userNo) {
        User user = userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getRole().getName() != Role.RoleType.CLIENT_USER) {
            throw new CustomException(ErrorCode.ONLY_CLIENT_USER_DELETABLE);
        }

        user.setCompany(null);
        userRepository.save(user);
    }
}