package kshrd.group2.article_mgmt.service;

import kshrd.group2.article_mgmt.model.dto.request.CommentRequest;
import kshrd.group2.article_mgmt.model.dto.response.CommentResponse;

/**
 * Developed by ChhornSeyha
 * Date: 27/08/2025
 */

public interface CommentService {
    void deleteComment(Long id);
    CommentResponse getCommentById (Long commentId);
    CommentResponse update (Long commentId, CommentRequest commentRequest);

}
