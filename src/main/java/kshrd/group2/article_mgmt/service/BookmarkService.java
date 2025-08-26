package kshrd.group2.article_mgmt.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kshrd.group2.article_mgmt.model.entity.Bookmark;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BookmarkService {
    List<Bookmark> getAllBookmarks(@Valid @Min(value = 1, message = "Page must be greater than 0") Integer page, @Valid @Min(value = 1, message = "Size must be greater than 0") Integer size, Sort.Direction direction);

    Bookmark createBookmark(Long articleId);

    void deleteBookmark(Long articleId);
}
