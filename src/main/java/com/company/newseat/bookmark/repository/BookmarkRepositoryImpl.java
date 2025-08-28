package com.company.newseat.bookmark.repository;

import com.company.newseat.bookmark.domain.Bookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.company.newseat.bookmark.domain.QBookmark.bookmark;

@RequiredArgsConstructor
@Repository
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Bookmark> findBookmarksByUserWithCursor(Long userId, Long lastBookmarkId, int size) {

        var query = queryFactory
                .selectFrom(bookmark)
                .where(bookmark.user.userId.eq(userId))
                .orderBy(bookmark.bookmarkId.desc())
                .limit(size + 1);

        if (lastBookmarkId != null && lastBookmarkId != 0) {
            query.where(bookmark.bookmarkId.lt(lastBookmarkId));
        }

        return query.fetch();
    }
}
