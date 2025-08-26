package kshrd.group2.article_mgmt.repository;

import kshrd.group2.article_mgmt.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Developed by ChhornSeyha
 * Date: 26/08/2025
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
