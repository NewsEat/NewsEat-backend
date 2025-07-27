package com.company.newseat.user.repository;

import com.company.newseat.user.domain.CategoryPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPreferenceRepository extends JpaRepository<CategoryPreference, Long> {
}
