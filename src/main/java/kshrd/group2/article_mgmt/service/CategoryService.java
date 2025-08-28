package kshrd.group2.article_mgmt.service;

import jakarta.validation.constraints.Positive;
import kshrd.group2.article_mgmt.model.dto.request.CategoryRequest;
import kshrd.group2.article_mgmt.model.dto.response.CategoryResponse;
import kshrd.group2.article_mgmt.model.dto.response.PagedResponse;
import kshrd.group2.article_mgmt.model.enumeration.CategoryProperty;
import org.springframework.data.domain.Sort;

public interface CategoryService {

    PagedResponse<CategoryResponse> getAllCategory(Integer page, Integer size, CategoryProperty categoryProperty, Sort.Direction direction);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    void deleteCategory(Long id);

    void increaseAmountOfCategoryArticle(Long categoryId);
    void decreaseAmountOfCategoryArticle(Long categoryId);
}
