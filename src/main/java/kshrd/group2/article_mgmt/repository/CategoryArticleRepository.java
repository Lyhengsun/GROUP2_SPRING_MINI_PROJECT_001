package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.CategoryArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryArticleRepository extends JpaRepository<CategoryArticle, Long> {
    Integer countByCategoryCategoryId(Long id);
}
