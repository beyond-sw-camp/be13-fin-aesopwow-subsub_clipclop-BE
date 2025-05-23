package com.aesopwow.subsubclipclop.domain.auth.service;

import com.aesopwow.subsubclipclop.domain.auth.dto.LoginRequestDTO;
import com.aesopwow.subsubclipclop.domain.auth.dto.request.SignUpRequestDto;
import com.aesopwow.subsubclipclop.domain.auth.dto.response.TokenResponseDto;
import com.aesopwow.subsubclipclop.domain.auth.jwt.JwtTokenProvider;
import com.aesopwow.subsubclipclop.domain.user.repository.UserRepository;
import com.aesopwow.subsubclipclop.domain.company.repository.CompanyRepository;
import com.aesopwow.subsubclipclop.domain.role.repository.RoleRepository;
import com.aesopwow.subsubclipclop.entity.Company;
import com.aesopwow.subsubclipclop.entity.Role;
import com.aesopwow.subsubclipclop.entity.User;
import com.aesopwow.subsubclipclop.global.enums.ErrorCode;
import com.aesopwow.subsubclipclop.global.exception.CustomException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    // 로그인 로직
    public TokenResponseDto login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("유효하지 않은 이메일입니다."));

        // 🔒 탈퇴 계정인지 확인
        if (user.getIsDeleted()) {
            throw new BadCredentialsException("탈퇴한 계정입니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken  = jwtTokenProvider.createAccessToken(
                user.getEmail(),
                user.getRole().getName().toString()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void logout(String bearerToken) {
        String accessToken = jwtTokenProvider.resolveToken(bearerToken);

        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.ACCESS_TOKEN_INVALID);
        }

        jwtTokenProvider.addBlacklist(accessToken);
        jwtTokenProvider.deleteRefreshToken(accessToken);
    }

    // 이메일 중복 확인 이후 Redis에 플래그 저장
    public void markEmailAsChecked(String email) {
        redisTemplate.opsForValue().set("EMAIL_CHECKED:" + email, "true", 10, TimeUnit.MINUTES);
    }

    @Transactional
    // 이메일 중복 확인 여부 검증 포함
    public void sendOtp(String email, String password) {
        // 이메일 중복 확인 여부 체크
        String emailChecked = redisTemplate.opsForValue().get("EMAIL_CHECKED:" + email);
        if (emailChecked == null || !emailChecked.equals("true")) {
            throw new IllegalArgumentException("이메일 중복 확인을 먼저 진행해 주세요.");
        }

        // OTP 전송 완료 여부 확인 (이미 메일을 보낸 경우, 중복 전송 방지)
        String otpSent = redisTemplate.opsForValue().get("OTP_SENT:" + email);
        if (otpSent != null && otpSent.equals("true")) {
            throw new IllegalArgumentException("이미 OTP 메일이 전송되었습니다.");
        }

        // OTP 생성 및 저장
        String otp = generateOtp();
        redisTemplate.opsForValue().set(email, otp, 3, TimeUnit.MINUTES);
        // 수정
//        redisTemplate.opsForValue().set("PWD:" + email, password, 10, TimeUnit.MINUTES);
        String encodedPassword = passwordEncoder.encode(password);
        redisTemplate.opsForValue().set("PWD:" + email, encodedPassword, 10, TimeUnit.MINUTES);


        try {
            emailService.sendEmail(email, "OTP 인증번호", "귀하의 OTP 인증번호는 " + otp + "입니다. 3분 이내에 입력해주세요.");
            // OTP 메일 전송 완료 플래그 설정
            redisTemplate.opsForValue().set("OTP_SENT:" + email, "true", 3, TimeUnit.MINUTES);
        } catch (MessagingException e) {
            throw new RuntimeException("OTP 이메일 전송에 실패했습니다.", e);
        }
    }


    // 6자리 OTP 생성
    private String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(secureRandom.nextInt(10));
        }
        return otp.toString();
    }

    // OTP 인증
    public void verifyOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email);

        if (storedOtp == null) {
            throw new IllegalArgumentException("OTP 인증이 만료되었거나 존재하지 않습니다.");
        }

        if (!storedOtp.equals(otp)) {
            throw new IllegalArgumentException("OTP 인증 실패. 올바른 OTP를 입력해 주세요.");
        }

        redisTemplate.opsForValue().set("VERIFIED:" + email, "true", 3, TimeUnit.MINUTES);
    }

    // 회원가입 최종 처리
    // 회원가입 최종 처리
    @Transactional
    public void signUp(SignUpRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();

        // OTP 인증 여부 확인
        String verified = redisTemplate.opsForValue().get("VERIFIED:" + email);
        if (verified == null || !verified.equals("true")) {
            throw new IllegalArgumentException("OTP 인증이 완료되지 않았습니다.");
        }

        // 비밀번호 일치 여부 확인
//        String savedPassword = redisTemplate.opsForValue().get("PWD:" + email);
//        if (savedPassword == null || !savedPassword.equals(password)) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
        //수정
        String savedPassword = redisTemplate.opsForValue().get("PWD:" + email);
        if (savedPassword == null || !passwordEncoder.matches(password, savedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        // 이미 존재하는 이메일 체크
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 유효성 검사
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이며, 영문자와 특수문자를 포함해야 합니다.");
        }

        Long companyNo = 1L;
        Company company = companyRepository.findById(companyNo)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // USER 역할을 찾고 없으면 새로 생성
        Role role = roleRepository.findByName(Role.RoleType.USER);
        if (role == null) {
            // Role 객체가 없으면 새로 생성
            role = Role.builder()
                    .name(Role.RoleType.USER)
                    .build();
            roleRepository.save(role); // 새로 생성된 Role 저장
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        redisTemplate.opsForValue().set("PWD:" + email, encodedPassword);

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .role(role)  // 기본 USER 역할 설정
                .company(company)  // 회사 정보는 나중에 설정
                .loginedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        userRepository.save(user);

        // Redis 클린업
        redisTemplate.delete("VERIFIED:" + email);
        redisTemplate.delete(email);
        redisTemplate.delete("PWD:" + email);
        redisTemplate.delete("EMAIL_CHECKED:" + email);
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+\\-={}\\[\\]:\";'<>?,./]).{8,}$";
        return password.matches(regex);
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
