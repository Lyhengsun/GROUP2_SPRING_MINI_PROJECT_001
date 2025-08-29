package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Slice<Bookmark> findByUser_UserId(Long id, Pageable pageable);

    Optional<Bookmark> findByUser_UserIdAndArticle_ArticleId(Long userId, Long articleId);

    boolean existsByUser_UserIdAndArticle_ArticleId(Long userId, Long articleId);
}
