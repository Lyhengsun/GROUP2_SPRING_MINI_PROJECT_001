package kshrd.group2.article_mgmt.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentResponse {
    private Long articleId;
    private String title;
    private String description;
    private Long userId;
    private List<String> categories;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private List<CommentResponse> commentResponses;
}
