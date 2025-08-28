package kshrd.group2.article_mgmt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Developed by ChhornSeyha
 * Date: 28/08/2025
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    @NotBlank(message = "Content of comment is required")
    @Size(max = 500, message = "Comment content must be at most 500 characters")
    private String content;
}
