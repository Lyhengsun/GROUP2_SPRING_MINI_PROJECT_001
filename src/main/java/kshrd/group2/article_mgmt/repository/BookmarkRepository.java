package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Slice<Bookmark> findByUser_UserId(Long id, Pageable pageable);
}
