package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
