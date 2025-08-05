package com.company.newseat.auth.application;

import com.company.newseat.auth.dto.request.SendEmailCodeRequest;
import com.company.newseat.auth.dto.request.VerifyEmailCodeRequest;
import com.company.newseat.auth.dto.response.*;
import com.company.newseat.auth.util.TokenProvider;
import com.company.newseat.auth.dto.jwt.JwtUserDetails;
import com.company.newseat.auth.dto.jwt.Tokens;
import com.company.newseat.auth.dto.request.LoginRequest;
import com.company.newseat.auth.dto.request.SignUpRequest;
import com.company.newseat.category.domain.Category;
import com.company.newseat.category.repository.CategoryRepository;
import com.company.newseat.email.application.MailService;
import com.company.newseat.email.domain.EmailAuth;
import com.company.newseat.email.domain.type.BooleanType;
import com.company.newseat.email.domain.type.Purpose;
import com.company.newseat.email.repository.EmailAuthRepository;
import com.company.newseat.global.domain.type.Status;
import com.company.newseat.email.domain.vo.VerificationCodeMail;
import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.AuthHandler;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.user.application.UserService;
import com.company.newseat.user.domain.CategoryPreference;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.CategoryPreferenceRepository;
import com.company.newseat.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.newseat.auth.type.JwtValidationResult.VALID_JWT;
import static com.company.newseat.email.util.MailConstants.KEY_CODE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CategoryPreferenceRepository categoryPreferenceRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final EmailAuthRepository emailAuthRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {

        if (userRepository.existsByEmailAndStatus(request.email(), Status.ACTIVE)) {
            throw new GeneralException(ErrorStatus.USER_EMAIL_DUPLICATED);
        }

        if (!emailAuthRepository.existsByEmailAuthIdAndEmailAndPurposeAndIsChecked(
                request.emailAuthId(), request.email(), Purpose.of(1), BooleanType.T)) {
            throw new GeneralException(ErrorStatus.EMAIL_NOT_VERIFIED);
        }

        User user = request.toUser(passwordEncoder);
        userRepository.save(user);

        List<Category> categories = categoryRepository.findAllById(request.categoryIds());
        List<CategoryPreference> preferences = categories.stream()
                .map(category -> CategoryPreference.of(user, category))
                .collect(Collectors.toList());
        categoryPreferenceRepository.saveAll(preferences);

        return SignUpResponse.of(user);
    }

    /**
     * 로그인
     */
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        User user = userService.getUserByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthHandler(ErrorStatus.LOGIN_FAILED_INCORRECT_PASSWORD);
        }

        Tokens tokens = tokenProvider.generateToken(getJwtUserDetails(user.getUserId()));
        response.setHeader("Refresh-Token", tokens.refreshToken());

        return LoginResponse.of(tokens, user);
    }


    /**
     * 액세스 토큰 재발급
     */
    public ReissueResponse reissueToken(String refreshToken) {

        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7).trim();
        }

        if (tokenProvider.validateToken(refreshToken) == VALID_JWT) {
            Long userId = tokenProvider.getJwtUserDetails(refreshToken).userId();
            return ReissueResponse.from(tokenProvider.generateToken(getJwtUserDetails(userId)));
        } else {
            throw new AuthHandler(ErrorStatus.JWT_INVALID);
        }
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void withdraw(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,
                        () -> {
                            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
                        });
    }

    /**
     * 테스트용 토큰 발급
     */
    public String getTestToken(Long userId) {
        return tokenProvider.generateToken(getJwtUserDetails(userId)).accessToken();
    }

    public JwtUserDetails getJwtUserDetails(Long userId) {
        User user = userService.getUserById(userId);
        return JwtUserDetails.fromUser(user);
    }

    /**
     * 이메일 인증 코드 발송
     */
    @Transactional
    public EmailAuth sendCodeMail(SendEmailCodeRequest request) {
        String recipientEmail  = request.email();

        if (Purpose.of(request.purpose()).equals(Purpose.SIGNUP) &&
                userRepository.existsByEmailAndStatus(request.email(), Status.ACTIVE))
            throw new AuthHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);

        VerificationCodeMail codeMail = new VerificationCodeMail();
        mailService.sendMail(recipientEmail, codeMail);

        EmailAuth emailAuth = SendEmailCodeRequest.toEmailAuth(request);
        emailAuth.setCode(codeMail.getValues().get(KEY_CODE));

        return emailAuthRepository.save(emailAuth);
    }

    /**
     * 이메일 인증 코드 일치 검증
     */
    @Transactional
    public boolean verifyEmailCode(VerifyEmailCodeRequest request) {

        EmailAuth emailAuth = emailAuthRepository.findById(request.emailAuthId())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.EMAIL_AUTH_NOT_FOUND));

        if (!request.emailAuthCode().equals(emailAuth.getCode())) {
            throw new AuthHandler(ErrorStatus.EMAIL_VERIFICATION_CODE_INVALID);
        }

        boolean timeCheck = Duration.between(emailAuth.getCreatedDate(), LocalDateTime.now()).getSeconds() < VerificationCodeMail.CODE_VALID_DURATION_SECONDS;
        if (!timeCheck) throw new AuthHandler(ErrorStatus.EMAIL_VERIFICATION_EXPIRED);

        emailAuth.setIsChecked(true);
        return true;
    }
}
