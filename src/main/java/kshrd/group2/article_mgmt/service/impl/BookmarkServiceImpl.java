package kshrd.group2.article_mgmt.service.impl;

import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.response.BookmarkResponse;
import kshrd.group2.article_mgmt.model.entity.Article;
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
    public List<BookmarkResponse> getAllBookmarks(Integer page, Integer size, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, "bookmarkId").ignoreCase());
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Slice<Bookmark> bookmarks = bookmarkRepository.findByUser_UserId(2L, pageable);

        return bookmarks.getContent().stream()
                .map(Bookmark::toResponse)
                .toList();
    }

    @Override
    public BookmarkResponse addBookmark(Long articleId) {
        Bookmark existingBookmark = bookmarkRepository.findByArticle_ArticleId(articleId).orElse(null);

//        Article article = articleRepository.findById(articleId)
//                .orElseThrow(() -> new NotFoundException("Article not found with id: " + articleId));

        if (existingBookmark != null && existingBookmark.getUser().getUserId() == 2L) {
            throw new BadRequestException("This article has been already added");
        }

        Bookmark bookmark = Bookmark.builder()
                .article(existingBookmark.getArticle())
//                .article(article)
                .user(existingBookmark.getUser())
//                .user(currentUser)
                .build();

        return bookmarkRepository.save(bookmark).toResponse();
    }

    @Override
    public void deleteBookmark(Long articleId) {
        Bookmark bookmark = bookmarkRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("No bookmark found for article with id: " + articleId));

        bookmarkRepository.delete(bookmark);
    }
}
