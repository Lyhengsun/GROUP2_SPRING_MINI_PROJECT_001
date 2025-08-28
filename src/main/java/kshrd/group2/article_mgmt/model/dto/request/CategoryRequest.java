package kshrd.group2.article_mgmt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kshrd.group2.article_mgmt.model.entity.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String categoryName;

    public Category toEntity() {
        return Category.builder()
                .categoryId(null)
                .categoryName(this.categoryName)
                .build();
    }
}
