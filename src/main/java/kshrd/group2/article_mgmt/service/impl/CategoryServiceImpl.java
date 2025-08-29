package kshrd.group2.article_mgmt.service.impl;

import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.exception.DataConflictException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.request.CategoryRequest;
import kshrd.group2.article_mgmt.model.dto.response.CategoryResponse;
import kshrd.group2.article_mgmt.model.dto.response.PagedResponse;
import kshrd.group2.article_mgmt.model.dto.response.PaginationInfo;
import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.model.entity.Category;
import kshrd.group2.article_mgmt.model.enumeration.CategoryProperty;
import kshrd.group2.article_mgmt.repository.CategoryRepository;
import kshrd.group2.article_mgmt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findCategoryByCategoryIdAndUserUserId(id,
                getCurrentUser().getUserId()).orElseThrow(() ->
                new NotFoundException("Category with id of "+id+" not found"));
    }

    private Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByCategoryNameAndUserUserId(name, getCurrentUser().getUserId());
    }

    @Override
    public PagedResponse<CategoryResponse> getAllCategory(Integer page, Integer size, CategoryProperty categoryProperty, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(direction, categoryProperty.getFieldName()));
        Page<Category> categories = categoryRepository.findAllByUserUserId(getCurrentUser().getUserId(), pageable);
        return PagedResponse.<CategoryResponse>builder()
                .items(categories.stream().map(Category::toResponse).toList())
                .paginationInfo(new PaginationInfo(categories))
                .build();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findCategoryByCategoryIdAndUserUserId(id, getCurrentUser().getUserId()).orElseThrow(() ->
                new NotFoundException("Category with id of "+id+" not found")).toResponse();
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (findCategoryByName(categoryRequest.getCategoryName()) != null) throw new BadRequestException("Category already exists, Please choose a different category name");
        Category category = categoryRequest.toEntity();
        category.setUser(getCurrentUser());
        return categoryRepository.save(category).toResponse();
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        if (findCategoryByName(categoryRequest.getCategoryName()) != null) throw new BadRequestException("Category already exists, Please choose a different category name");
        Category category = findCategoryById(id);
        category.setCategoryName(categoryRequest.getCategoryName());
        return categoryRepository.save(category).toResponse();
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = findCategoryById(id);
        if (category.getAmountOfArticle() > 0) throw new DataConflictException("This category is already in use");
        categoryRepository.delete(category);
    }

    @Override
    public Category increaseAmountOfCategoryArticle(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.setAmountOfArticle(category.getAmountOfArticle()+1);
        return categoryRepository.save(category);
    }

    @Override
    public Category decreaseAmountOfCategoryArticle(Long categoryId) {
        Category category = findCategoryById(categoryId);
        if (category.getAmountOfArticle() > 0) {
            category.setAmountOfArticle(category.getAmountOfArticle()-1);
            return categoryRepository.save(category);
        }
        return category;
    }
}
