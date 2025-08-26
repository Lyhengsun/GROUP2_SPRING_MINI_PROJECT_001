package kshrd.group2.article_mgmt.service.impl;

import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.entity.Bookmark;
import kshrd.group2.article_mgmt.repository.BookmarkRepository;
import kshrd.group2.article_mgmt.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Override
    public List<Bookmark> getAllBookmarks(Integer page, Integer size, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, "createdAt").ignoreCase());
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Slice<Bookmark> bookmarks = bookmarkRepository.findByUser_UserId(3L, pageable);

        return bookmarks.getContent();
    }

    @Override
    public Bookmark createBookmark(Long articleId) {
        return null;
    }

    @Override
    public void deleteBookmark(Long articleId) {
        Bookmark bookmark = bookmarkRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("No bookmark found for article with id: " + articleId));

        bookmarkRepository.delete(bookmark);
    }
}
