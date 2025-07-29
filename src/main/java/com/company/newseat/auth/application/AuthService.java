package com.company.newseat.auth.application;

import com.company.newseat.auth.util.TokenProvider;
import com.company.newseat.auth.dto.jwt.JwtUserDetails;
import com.company.newseat.auth.dto.jwt.Tokens;
import com.company.newseat.auth.dto.request.LoginRequest;
import com.company.newseat.auth.dto.request.SignUpRequest;
import com.company.newseat.auth.dto.response.LoginResponse;
import com.company.newseat.auth.dto.response.ReissueResponse;
import com.company.newseat.auth.dto.response.SignUpResponse;
import com.company.newseat.category.domain.Category;
import com.company.newseat.category.repository.CategoryRepository;
import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.AuthHandler;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.user.application.UserService;
import com.company.newseat.user.domain.CategoryPreference;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.domain.type.Provider;
import com.company.newseat.user.repository.CategoryPreferenceRepository;
import com.company.newseat.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.company.newseat.auth.type.JwtValidationResult.VALID_JWT;

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

    /**
     * 회원 가입
     */
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (!userRepository.existsByEmailAndProvider(request.email(), Provider.SELF)) {
            User user = request.toUser(passwordEncoder);
            userRepository.save(user);

            List<Category> categories = categoryRepository.findAllById(request.categoryIds());

            List<CategoryPreference> preferences = categories.stream()
                    .map(category -> CategoryPreference.of(user, category))
                    .collect(Collectors.toList());

            categoryPreferenceRepository.saveAll(preferences);

            return SignUpResponse.of(user);
        } else {
            throw new GeneralException(ErrorStatus.USER_EMAIL_DUPLICATED);
        }
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
}
