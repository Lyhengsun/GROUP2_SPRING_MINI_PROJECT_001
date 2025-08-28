package kshrd.group2.article_mgmt.service.impl;

import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.response.BookmarkResponse;
import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.model.entity.Article;
import kshrd.group2.article_mgmt.model.entity.Bookmark;
import kshrd.group2.article_mgmt.repository.ArticleRepository;
import kshrd.group2.article_mgmt.repository.BookmarkRepository;
import kshrd.group2.article_mgmt.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;

    @Override
    public List<BookmarkResponse> getAllBookmarks(Integer page, Integer size, Sort.Direction direction) {
        Sort sort = Sort.by(direction, "editedAt", "createdAt", "bookmarkId");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Slice<Bookmark> bookmarks = bookmarkRepository.findByUser_UserId(getCurrentUser().getUserId(), pageable);

        return bookmarks.getContent().stream()
                .map(Bookmark::toResponse)
                .toList();
    }

    @Override
    public BookmarkResponse addBookmark(Long articleId) {
        if (bookmarkRepository.existsByUser_UserIdAndArticle_ArticleId(getCurrentUser().getUserId(), articleId)) {
            throw new BadRequestException("This article has been already added");
        }

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article not found with id: " + articleId));

        Bookmark bookmark = Bookmark.builder()
                .article(article)
                .user(getCurrentUser())
                .build();

        return bookmarkRepository.save(bookmark).toResponse();
    }

    @Override
    public void deleteBookmark(Long articleId) {
        Bookmark bookmark = bookmarkRepository.findByUser_UserIdAndArticle_ArticleId(getCurrentUser().getUserId(), articleId)
                .orElseThrow(() -> new NotFoundException("No bookmark found for article with id: " + articleId));

        bookmarkRepository.delete(bookmark);
    }

    private AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
