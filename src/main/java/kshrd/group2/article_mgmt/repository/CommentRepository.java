package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.dto.response.CommentResponse;
import kshrd.group2.article_mgmt.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Developed by ChhornSeyha
 * Date: 26/08/2025
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(value = "select c from Comment c where c.commentId = :commentId AND c.user.userId = :userId")
    CommentResponse getCommentById (Long commentId, Long userId);

}
