package kshrd.group2.article_mgmt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotEmpty(message = "At least one category must be selected")
    private List<Long> categoryIds;
}