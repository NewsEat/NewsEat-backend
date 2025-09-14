package com.company.newseat.bookmark.repository;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {
    boolean existsByUserAndTitle(User user, String title);

    boolean existsByUserAndNewsId(User user, Long newsId);

    Optional<Bookmark> findByUserAndNewsId(User user, Long newsId);
}
