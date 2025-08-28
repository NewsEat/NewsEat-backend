package com.company.newseat.bookmark.repository;

import com.company.newseat.bookmark.domain.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {

    List<Bookmark> findBookmarksByUserWithCursor(Long userId, Long lastBookmarkId, int size);

}
