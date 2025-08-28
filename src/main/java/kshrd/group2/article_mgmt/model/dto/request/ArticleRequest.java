package kshrd.group2.article_mgmt.model.dto.request;

import jakarta.validation.constraints.*;
import kshrd.group2.article_mgmt.model.entity.Article;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequest {
    @Size(max = 100, message = "Title must be at most 100 characters")
    @NotBlank(message = "Title must not be null")
    private String title;

    private String description;

    @NotEmpty(message = "categoryIds must not be null")
    private List<@NotNull(message = "Categories not found: [null]") @Positive Long> categoryIds;

    public Article toArticleEntity() {
        return new Article(null, this.title, this.description, null, null);
    }
}
