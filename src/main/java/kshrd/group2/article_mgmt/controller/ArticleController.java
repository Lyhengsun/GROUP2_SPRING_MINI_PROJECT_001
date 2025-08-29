package kshrd.group2.article_mgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kshrd.group2.article_mgmt.model.dto.request.ArticleRequest;
import kshrd.group2.article_mgmt.model.dto.request.CommentRequest;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.dto.response.ArticleCommentResponse;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import kshrd.group2.article_mgmt.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ArticleController extends BaseController{

    private final ArticleService articleService;

    // Create new article, can be used by only AUTHOR role
    @PostMapping
    @Operation(summary = "Create new article, can be used by only AUTHOR role")
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(@RequestBody @Valid ArticleRequest articleRequest) {
        return responseEntity("Article created successfully", HttpStatus.CREATED, articleService.createArticle(articleRequest));
    }

    //Get an article by id that allows all roles
    @GetMapping("/{articleId}")
    @Operation(summary = "Get an article by id.", description = "Can be use by all role")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticleById(@PathVariable("articleId") Long articleId){
        return responseEntity("Article with id: " + articleId + " is fetched successfully", HttpStatus.FOUND, articleService.getArticleById(articleId));
    }

    //Get all articles that allow all roles
    @GetMapping
    @Operation(summary = "Get all articles", description = "Can be use by all role")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> listAllArticles(@RequestParam(defaultValue = "1") @Positive int page, @RequestParam(defaultValue = "10") int size, ArticleProperties articleProperties, Sort.Direction direction){
        return responseEntity("All Articles has been fetch successfully!", HttpStatus.FOUND, articleService.listAllArticles(page, size, articleProperties, direction));
    }

    @PostMapping("/{articleId}/comment")
    @Operation(summary = "Comment on specific article. can be used create comment on specific article by all roles")
    public ResponseEntity<ApiResponse<ArticleCommentResponse>> createComment(@Positive @PathVariable("articleId") Long id, @RequestBody @Valid CommentRequest commentRequest) {
        return responseEntity("Create comment successfully", HttpStatus.CREATED, articleService.createComment(id, commentRequest));
    }

    @GetMapping("/{articleId}/comment")
    @Operation(summary = "Get all comments by article id. can be used by all roles")
    public ResponseEntity<ApiResponse<ArticleCommentResponse>> getAllCommentByArticleId(@Positive @PathVariable("articleId") Long id) {
        return responseEntity("", HttpStatus.OK, articleService.getAllCommentByArticleId(id));
    }

    //Delete an article by ID that allow only an AUTHOR role
    @DeleteMapping("/{articleId}")
    @Operation(summary = "Delete an article by ID" , description = "Can be use by only AUTHOR role")
    public ResponseEntity<ApiResponse<ArticleResponse>> deleteArticleById(@Positive @PathVariable Long articleId){
        articleService.deleteArticleById(articleId);
        return responseEntity("Article with id: " + articleId + " is deleted successfully", HttpStatus.OK, null);
    }

    //Update an article by id that allows only an AUTHOR role
    @PutMapping("/{articleId}")
    @Operation(summary = "Update an article by id. Can be use by only AUTHOR role")
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticleById(@Positive @PathVariable Long articleId, @Valid @RequestBody ArticleRequest request){
        return responseEntity("Article with id: " + articleId + " is updated successfully", HttpStatus.OK, articleService.updateArticleById(articleId, request));
    }
}