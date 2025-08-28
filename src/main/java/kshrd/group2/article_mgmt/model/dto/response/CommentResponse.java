package kshrd.group2.article_mgmt.model.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Developed by ChhornSeyha
 * Date: 28/08/2025
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonPropertyOrder({"commentId", "content", "createdAt", "editedAt", "userId"})
public class CommentResponse extends BaseEntityAuditResponse {
    private Long commentId;
    private String content;
    private Long userId;
}
