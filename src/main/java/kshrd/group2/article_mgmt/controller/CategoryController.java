package kshrd.group2.article_mgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kshrd.group2.article_mgmt.model.dto.request.CategoryRequest;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.CategoryResponse;
import kshrd.group2.article_mgmt.model.dto.response.PagedResponse;
import kshrd.group2.article_mgmt.model.enumeration.CategoryProperty;
import kshrd.group2.article_mgmt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/categories")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category Controller")
public class CategoryController extends BaseController{
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<PagedResponse<CategoryResponse>>> getAllCategory(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "CATEGORYID") CategoryProperty categoryProperty,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return responseEntity("Get all categories successfully",
                categoryService.getAllCategory(page, size, categoryProperty, direction));
    }

    @GetMapping("{category-id}")
    @Operation(summary = "Get category by id. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable("category-id") Long id) {
        return responseEntity("Get category successfully", categoryService.getCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "Create new category. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        return responseEntity("Created new category successfully", categoryService.createCategory(categoryRequest));
    }

    @PutMapping("{category-id}")
    @Operation(summary = "Update category. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable("category-id") Long id ,
                                                                        @RequestBody @Valid CategoryRequest categoryRequest) {
        return responseEntity("Updated category successfully", categoryService.updateCategory(id, categoryRequest));
    }

    @DeleteMapping("{category-id}")
    @Operation(summary = "Delete category. can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<CategoryResponse>> deleteCategory(@PathVariable("category-id") Long id) {
        categoryService.deleteCategory(id);
        return responseEntity("Deleted category successfully");
    }

}
