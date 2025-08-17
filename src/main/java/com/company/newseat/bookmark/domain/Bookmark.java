package com.company.newseat.bookmark.domain;

import com.company.newseat.news.domain.type.Sentiment;
import com.company.newseat.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
