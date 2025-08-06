package com.company.newseat.user.repository.init;

import com.company.newseat.global.domain.type.Status;
import com.company.newseat.global.util.DataInit;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.domain.type.Provider;
import com.company.newseat.user.domain.type.Role;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@DataInit
@Order(2)
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) {

        if (userRepository.count() > 0) {
            log.info("[User] 사용자 더미 데이터 존재");
        } else {
            List<User> memberList = new ArrayList<>();

            User DUMMY_GUEST = User.builder()
                    .email("guest@gmail.com")
                    .password(passwordEncoder.encode("guest0000"))
                    .provider(Provider.SELF)
                    .role(Role.GUEST)
                    .nickname("guest")
                    .isDetox(false)
                    .status(Status.ACTIVE)
                    .build();

            User DUMMY_USER = User.builder()
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("user0000"))
                    .provider(Provider.SELF)
                    .role(Role.USER)
                    .nickname("user")
                    .isDetox(true)
                    .status(Status.ACTIVE)
                    .build();

            User DUMMY_ADMIN = User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin0000"))
                    .provider(Provider.SELF)
                    .role(Role.ADMIN)
                    .nickname("admin")
                    .isDetox(false)
                    .status(Status.ACTIVE)
                    .build();

            memberList.add(DUMMY_GUEST);
            memberList.add(DUMMY_USER);
            memberList.add(DUMMY_ADMIN);

            userRepository.saveAll(memberList);
        }
    }
}
