package com.company.newseat.category.domain;

import com.company.newseat.news.domain.News;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "order_index")
    private int orderIndex;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<News> newsList = new ArrayList<>();

    @Builder
    public Category(String name, int orderIndex) {
        this.name = name;
        this.orderIndex = orderIndex;
    }
}
