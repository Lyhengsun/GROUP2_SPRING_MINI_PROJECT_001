package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.categoryName = :name and c.user.userId = :userId")
    Category findCategoryByCategoryNameAndUserUserId(String name, Long userId);

    Page<Category> findAllByUserUserId(Long id, Pageable pageable);

    List<Category> findAllByUserUserId(Long id);

    Optional<Category> findCategoryByCategoryIdAndUserUserId(Long categoryId, Long userId);

    Boolean existsCategoryByCategoryId(Long id);
    List<Category> findAllByUserUserId(Long id);
    List<Category> findAllByUserUserIdAndCategoryIdIn(Long userId, List<Long> categoryIds);

}
