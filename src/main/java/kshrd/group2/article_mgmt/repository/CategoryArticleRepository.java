package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.Article;
import kshrd.group2.article_mgmt.model.entity.CategoryArticle;
import kshrd.group2.article_mgmt.service.CategoryService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryArticleRepository extends JpaRepository<CategoryArticle, Long> {
    void deleteByArticle(Article article);
    Integer countByCategoryCategoryId(Long id);
}
