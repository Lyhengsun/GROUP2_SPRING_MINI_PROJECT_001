package kshrd.group2.article_mgmt.service.impl;

import kshrd.group2.article_mgmt.exception.AccessDeniedException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.request.CommentRequest;
import kshrd.group2.article_mgmt.model.dto.response.CommentResponse;
import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.model.entity.Comment;
import kshrd.group2.article_mgmt.model.enumeration.UserRole;
import kshrd.group2.article_mgmt.repository.CommentRepository;
import kshrd.group2.article_mgmt.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Developed by ChhornSeyha
 * Date: 27/08/2025
 */

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Override
    public void deleteComment(Long commentId) {
        AppUser currentUser = getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with ID: " + commentId));

        if(!comment.getUser().getUserId().equals(currentUser.getUserId())){
            throw new AccessDeniedException("you do not have permission to delete this commend. ");
        }
            commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        AppUser currentUser = getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with ID: " + commentId));

        if (!comment.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You do not have permission to view this comment.");
        }
        return comment.toResponse();
    }

    @Override
    public CommentResponse update(Long commentId, CommentRequest commentRequest) {
        AppUser currentUser = getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with ID: " + commentId));
        if (!comment.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You do not have permission to view this comment.");
        }
        comment.setContent(commentRequest.getContent());
        comment.setEditedAt(LocalDateTime.now());
        commentRepository.save(comment);

        return comment.toResponse();
    }
}
