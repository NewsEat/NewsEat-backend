package com.company.newseat.email.repository;

import com.company.newseat.email.domain.EmailAuth;
import com.company.newseat.email.domain.type.BooleanType;
import com.company.newseat.email.domain.type.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    boolean existsByEmailAuthIdAndEmailAndPurposeAndIsChecked(Long emailAuthId, String email, Purpose purpose, BooleanType isChecked);
}
