package kshrd.group2.article_mgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kshrd.group2.article_mgmt.model.dto.request.CommentRequest;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.CommentResponse;
import kshrd.group2.article_mgmt.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Developed by ChhornSeyha
 * Date: 28/08/2025
 */

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CommentController extends BaseController{

    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment by `CommendId`, can delete your only comment")
    public ResponseEntity<ApiResponse<Void>> delete (@PathVariable("commentId") @Min(value = 1, message = "Comment ID must be a positive number.") Long commentId ){
        commentService.deleteComment(commentId);
        return responseEntity("Deleted comment successfully");
    }
    @GetMapping("/{commentId}")
    @Operation(summary = "Get comment by `CommendId`, can view your only comment")
    public ResponseEntity<ApiResponse<CommentResponse>> getCommentId(@PathVariable("commentId") @Min(value = 1, message = "Comment ID must be a positive number.") Long commentId){
        return responseEntity("Get comment successfully",HttpStatus.OK,commentService.getCommentById(commentId));
    }

    @PutMapping("update/{commentId}")
    @Operation(summary = "Update comment by `CommendId`, can update your only comment")
    public ResponseEntity<ApiResponse<CommentResponse>> update (@PathVariable("commentId") @Min(value = 1, message = "Comment ID must be a positive number.") Long commentId, @Valid @RequestBody CommentRequest commentRequest){
        return responseEntity("Updated comment successfully",HttpStatus.OK,commentService.update(commentId,commentRequest));
    }
}
