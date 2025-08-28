package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository <Article, Long>{
}
