package com.company.newseat.user.repository;

import com.company.newseat.global.domain.type.Status;
import com.company.newseat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmailAndStatus(String email, Status status);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.preferences p " +
            "JOIN FETCH p.category " +
            "WHERE u.userId = :userId")
    Optional<User> findByIdWithPreferencesAndCategory(@Param("userId") Long userId);
}
