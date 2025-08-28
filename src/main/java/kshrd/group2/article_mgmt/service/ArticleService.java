package kshrd.group2.article_mgmt.service;

import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ArticleService {
    ArticleResponse getArticleById(Long articleId);

    List<ArticleResponse> listAllArticles(int page, int size, ArticleProperties articleProperties, Sort.Direction direction);
}
