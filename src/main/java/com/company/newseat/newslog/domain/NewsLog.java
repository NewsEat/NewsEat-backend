package com.company.newseat.newslog.domain;

import com.company.newseat.global.domain.BaseTimeEntity;
import com.company.newseat.news.domain.News;
import com.company.newseat.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 기능 구현 방식에 따라 속성 추후 추가 가능
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NewsLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

}
