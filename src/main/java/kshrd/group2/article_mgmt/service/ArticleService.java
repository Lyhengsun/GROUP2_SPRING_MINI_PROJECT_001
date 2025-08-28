package kshrd.group2.article_mgmt.service;

import jakarta.validation.Valid;
import kshrd.group2.article_mgmt.model.dto.request.ArticleRequest;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ArticleService {
    ArticleResponse getArticleById(Long articleId);

    List<ArticleResponse> listAllArticles(int page, int size, ArticleProperties articleProperties, Sort.Direction direction);

    ArticleResponse createArticle(@Valid ArticleRequest articleRequest);
}
