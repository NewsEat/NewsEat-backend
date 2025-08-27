package com.company.newseat.bookmark.domain;

import com.company.newseat.news.domain.News;
import com.company.newseat.news.domain.type.Sentiment;
import com.company.newseat.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "publisher")
    private String publisher;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment")
    private Sentiment sentiment;

    @Column(name = "published_at")
    private String published_at;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "category", length = 20)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Bookmark(String title, String content, String publisher, Sentiment sentiment, String published_at, String imgUrl, String category, User user) {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.sentiment = sentiment;
        this.published_at = published_at;
        this.imgUrl = imgUrl;
        this.category = category;
        this.user = user;
    }

    public static Bookmark fromNews(News news, User user) {
        return Bookmark.builder()
                .title(news.getTitle())
                .content(news.getContent())
                .publisher(news.getPublisher())
                .sentiment(news.getSentiment())
                .published_at(news.getPublished_at())
                .imgUrl(news.getImgUrl())
                .category(news.getCategory().getName())
                .user(user)
                .build();
    }
}
