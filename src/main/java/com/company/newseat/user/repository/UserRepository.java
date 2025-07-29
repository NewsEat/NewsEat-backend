package com.company.newseat.user.repository;

import com.company.newseat.user.domain.User;
import com.company.newseat.user.domain.type.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmailAndProvider(String email, Provider provider);
}
