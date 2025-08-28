package kshrd.group2.article_mgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import kshrd.group2.article_mgmt.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    //Get an article by id that allows all roles
    @GetMapping("/{articleId}")
    @Operation(summary = "Get an article by id.", description = "Can be use by all role")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticleById(@PathVariable("articleId") Long articleId){
        return responseEntity("Article with id: " + articleId + " is fetched successfully", HttpStatus.FOUND, articleService.getArticleById(articleId));
    }

    //Get all articles that allow all roles
    @GetMapping
    @Operation(summary = "Get all articles", description = "Can be use by all role")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> listAllArticles(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, ArticleProperties articleProperties, Sort.Direction direction){
        return responseEntity("All Articles has been fetch successfully!", HttpStatus.FOUND, articleService.listAllArticles(page, size, articleProperties, direction));
    }

}