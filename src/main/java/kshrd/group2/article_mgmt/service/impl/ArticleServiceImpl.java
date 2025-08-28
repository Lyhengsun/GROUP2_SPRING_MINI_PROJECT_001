package kshrd.group2.article_mgmt.service.impl;

import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.exception.DataConflictException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.request.ArticleRequest;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.dto.response.PagedResponse;
import kshrd.group2.article_mgmt.model.dto.response.PaginationInfo;
import kshrd.group2.article_mgmt.model.entity.Article;
import kshrd.group2.article_mgmt.model.entity.Category;
import kshrd.group2.article_mgmt.model.entity.CategoryArticle;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import kshrd.group2.article_mgmt.repository.ArticleRepository;
import kshrd.group2.article_mgmt.repository.CategoryArticleRepository;
import kshrd.group2.article_mgmt.repository.CategoryRepository;
import kshrd.group2.article_mgmt.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryArticleRepository categoryArticleRepository;

    @Override
    public ArticleResponse getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article with id: " + articleId + " is not found"))
                .toResponse();
    }

    @Override
    public List<ArticleResponse> listAllArticles(int page, int size, ArticleProperties articleProperties, Sort.Direction direction) {

        Page<Article> articlePage = articleRepository.findAll(PageRequest.of(page - 1, size, Sort.by(direction, articleProperties.getFieldName())));

        return articlePage.stream().map(Article::toResponse).toList();
    }

    @Override
    public ArticleResponse createArticle(ArticleRequest articleRequest) {

        // validate duplicate categoryIds
        List<Long> categoryIds = articleRequest.getCategoryIds();
        Set<Long> uniqueIds = new HashSet<>();
        Set<Long> duplicateItems = new HashSet<>();
        List<Long> notFoundIds = new ArrayList<>();

        for (Long id : categoryIds) {
            if (!uniqueIds.add(id)) {
                duplicateItems.add(id);
            }
        }
        if (!duplicateItems.isEmpty())
            throw new BadRequestException("Duplicate category ids: " + duplicateItems);

        // validate categoryIds not found
        for (Long id : categoryIds) {
            if (!categoryRepository.existsCategoryByCategoryId(id)) {
                notFoundIds.add(id);
            }
        }
        if (!notFoundIds.isEmpty())
            throw new BadRequestException("Categories not found: " + notFoundIds);

        // save articles
        Article savedArticle = articleRepository.save(articleRequest.toArticleEntity());

        // save category_articles
        for (Long id : categoryIds) {
            Category category = categoryRepository
                    .findById(id)
                    .orElseThrow(() -> new NotFoundException("Categories not found: " + id));

            CategoryArticle categoryArticle = CategoryArticle.builder()
                    .article(savedArticle)
                    .category(category)
                    .build();

            categoryArticleRepository.save(categoryArticle);
        }

        return articleRepository.findById(savedArticle.getArticleId())
                .orElseThrow(() -> new NotFoundException("Article with id: " + savedArticle.getArticleId() + " is not found"))
                .toResponse();
    }
}