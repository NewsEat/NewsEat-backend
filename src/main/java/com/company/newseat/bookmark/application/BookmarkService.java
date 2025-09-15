package com.company.newseat.bookmark.application;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.bookmark.dto.response.BookmarkResponse;
import com.company.newseat.bookmark.dto.response.BookmarkSimpleListResponse;
import com.company.newseat.bookmark.dto.response.BookmarkSimpleResponse;
import com.company.newseat.bookmark.dto.response.BookmarkSummaryResponse;
import com.company.newseat.bookmark.repository.BookmarkRepository;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.BookmarkHandler;
import com.company.newseat.global.exception.handler.NewsHandler;
import com.company.newseat.global.exception.handler.UserHandler;
import com.company.newseat.news.domain.News;
import com.company.newseat.news.repository.NewsRepository;
import com.company.newseat.news.util.NewsSummaryPromptProvider;
import com.company.newseat.user.domain.User;
import com.company.newseat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final OpenAiChatModel openAiChatModel;
    private final NewsSummaryPromptProvider promptProvider;
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

    /**
     * 북마크에서 삭제
     */
    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .filter(b -> b.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new BookmarkHandler(ErrorStatus.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }

    /**
     * 북마크한 뉴스 단건 조회
     */
    public BookmarkResponse getBookmark(Long userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .filter(b -> b.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new BookmarkHandler(ErrorStatus.BOOKMARK_NOT_FOUND));

        return BookmarkResponse.from(bookmark);
    }

    /**
     * 북마크 리스트 조회 (커서 기반)
     */
    public BookmarkSimpleListResponse getBookmarkList(Long userId, Long lastBookmarkId, int size) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByUserWithCursor(userId, lastBookmarkId, size);

        boolean hasMore = bookmarks.size() > size;

        List<BookmarkSimpleResponse> list = bookmarks.stream()
                .limit(size)
                .map(BookmarkSimpleResponse::from)
                .toList();

        return BookmarkSimpleListResponse.of(list, hasMore);
    }


    /**
     * open ai 이용한 북마크한 기사 내용 요약
     */
    public BookmarkSummaryResponse summarizeBookmark(Long bookmarkId) {

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NewsHandler(ErrorStatus.NEWS_NOT_FOUND));

        String title = bookmark.getTitle();
        String sentiment = bookmark.getSentiment().getDescription();

        String newsContent = bookmark.getContent();
        String summaryText = generateSummaryFromContent(newsContent);

        return BookmarkSummaryResponse.of(title, sentiment, summaryText);
    }

    private String generateSummaryFromContent(String content) {
        String promptText = promptProvider.createPrompt(content);
        try {
            return openAiChatModel.call(promptText);
        } catch (Exception e) {
            throw new NewsHandler(ErrorStatus.SUMMARY_GENERATION_FAILED);
        }
    }
}
