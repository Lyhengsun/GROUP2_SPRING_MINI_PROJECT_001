package kshrd.group2.article_mgmt.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BookmarkResponse extends BaseEntityAuditResponse {
    private Long articleId;
    private String title;
    private String description;
    private Long userId;
    private List<String> categories;
}
