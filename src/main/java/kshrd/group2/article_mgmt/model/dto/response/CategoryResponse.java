package kshrd.group2.article_mgmt.model.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CategoryResponse extends BaseEntityAuditResponse {
    private Long categoryId;
    private String categoryName;
    private Integer amountOfArticles;
}
