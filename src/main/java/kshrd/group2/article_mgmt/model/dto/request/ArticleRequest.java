package kshrd.group2.article_mgmt.model.dto.request;

import jakarta.validation.constraints.*;
import kshrd.group2.article_mgmt.model.entity.Article;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Title must contain only letters and numbers")
    private String title;

    @NotBlank(message = "Description is required")
    @Pattern(regexp = "^[a-zA-Z0-9 .,!?]*$", message = "Description must contain only letters, numbers, spaces, and basic punctuation")
    private String description;

    @NotEmpty(message = "categoryIds must not be null")
    private List<@NotNull(message = "Categories not found: [null]") @Positive Long> categoryIds;

    public Article toArticleEntity() {
        return new Article(null, this.title, this.description, null, null, null);
    }
}
