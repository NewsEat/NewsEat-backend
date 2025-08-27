package com.company.newseat.bookmark.application;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.bookmark.repository.BookmarkRepository;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.BookmarkHandler;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    /**
     * 사용자가 특정 뉴스를 북마크로 저장
     * (원본 뉴스 데이터를 스냅샷 형태로 Bookmark 엔티티에 저장)
     */
    @Transactional
    public Long addBookmark(Long userId, Long newsId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        News news = newsRepository.findByIdWithCategory(newsId)
                .orElseThrow(() -> new NewsHandler(ErrorStatus.NEWS_NOT_FOUND));

        if (bookmarkRepository.existsByUserAndTitle(user, news.getTitle())) {
            throw new BookmarkHandler(ErrorStatus.BOOKMARK_ALREADY_EXISTS);
        }

        Bookmark bookmark = Bookmark.fromNews(news, user);
        bookmarkRepository.save(bookmark);

        return bookmark.getBookmarkId();
    }

    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .filter(b -> b.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new BookmarkHandler(ErrorStatus.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }

}
