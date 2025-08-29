package kshrd.group2.article_mgmt.model.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleResponse {
    private Long articleId;
    private String title;
    private String description;
    private Long userId;
    private List<String> categories;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
