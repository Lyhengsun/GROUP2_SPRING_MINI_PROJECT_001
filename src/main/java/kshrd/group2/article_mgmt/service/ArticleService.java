package kshrd.group2.article_mgmt.service;

import jakarta.validation.Valid;
import kshrd.group2.article_mgmt.model.dto.request.ArticleRequest;
import jakarta.validation.constraints.Positive;
import kshrd.group2.article_mgmt.model.dto.request.CommentRequest;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.dto.response.ArticleCommentResponse;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ArticleService {
    ArticleResponse getArticleById(Long articleId);

    List<ArticleResponse> listAllArticles(int page, int size, ArticleProperties articleProperties, Sort.Direction direction);

    ArticleResponse createArticle(@Valid ArticleRequest articleRequest);

    void deleteArticleById(Long articleId);

    ArticleResponse updateArticleById(@Positive Long articleId, @Valid ArticleRequest request);

    ArticleCommentResponse createComment(@Positive Long id, @Valid CommentRequest commentRequest);

    ArticleCommentResponse getAllCommentByArticleId(@Positive Long id);
}
