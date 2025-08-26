package com.company.newseat.user.domain;

import com.company.newseat.bookmark.domain.Bookmark;
import com.company.newseat.global.domain.type.Status;
import com.company.newseat.newslog.domain.NewsLog;
import com.company.newseat.user.domain.type.Provider;
import com.company.newseat.user.domain.type.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "is_detox")
    private Boolean isDetox;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryPreference> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsLog> newsLogs = new ArrayList<>();

    @Builder
    public User(String nickname, String email, String password, Boolean isDetox,  Provider provider, Role role, Status status) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isDetox = isDetox;
        this.provider = provider;
        this.role = role;
        this.status = status;
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
