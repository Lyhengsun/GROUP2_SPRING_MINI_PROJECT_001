package kshrd.group2.article_mgmt.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String categoryName;
    private Integer amountOfArticles;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}