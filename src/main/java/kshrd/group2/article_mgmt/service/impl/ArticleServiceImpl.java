package kshrd.group2.article_mgmt.service.impl;

import jakarta.transaction.Transactional;
import kshrd.group2.article_mgmt.exception.ForbiddenException;
import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.exception.DataConflictException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.request.ArticleRequest;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.model.entity.Article;
import kshrd.group2.article_mgmt.model.entity.Category;
import kshrd.group2.article_mgmt.model.entity.CategoryArticle;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import kshrd.group2.article_mgmt.model.enumeration.UserRole;
import kshrd.group2.article_mgmt.repository.ArticleRepository;
import kshrd.group2.article_mgmt.repository.CategoryArticleRepository;
import kshrd.group2.article_mgmt.repository.CategoryRepository;
import kshrd.group2.article_mgmt.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryArticleRepository categoryArticleRepository;

    public AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

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

        // get current user
        AppUser user = getCurrentUser();

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

        // prepare data article before save
        Article article = articleRequest.toArticleEntity();
        article.setUser(user);

        // initial category article list for saving in Category
        List<CategoryArticle> categoryArticleList = new ArrayList<>();

        // save category_articles
        for (Long id : categoryIds) {
            Category category = categoryRepository
                    .findById(id)
                    .orElseThrow(() -> new NotFoundException("Categories not found: " + id));

            CategoryArticle categoryArticle = CategoryArticle.builder()
                    .article(article) // bidirectional link
                    .category(category)
                    .build();

            categoryArticleList.add(categoryArticle);
        }
        article.setCategoryArticles(categoryArticleList);

        // save article
        Article savedArticle = articleRepository.save(article);

        return savedArticle.toResponse();
    }

    @Override
    @Transactional
    public void deleteArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article with id: " + articleId + " is not found"));
        articleRepository.delete(article);
    }

    @Override
    @Transactional
    public ArticleResponse updateArticleById(Long articleId, ArticleRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException("Article with id: " + articleId + " is not found"));

        if (getCurrentUser().getRole() != UserRole.ROLE_AUTHOR && !article.getUser().getUserId().equals(getCurrentUser().getUserId())) {
            throw new ForbiddenException("You don’t have permission to update this article");
        }
        article.setTitle(request.getTitle());
        article.setDescription(request.getDescription());

        categoryArticleRepository.deleteByArticle(article);
        categoryArticleRepository.flush();

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());

        for (Long req : request.getCategoryIds()) {
            if (!categories.contains(categoryRepository.findById(req).orElseThrow(() ->
                    new NotFoundException("Category with id: " + req + " is not found")))) {
                throw new NotFoundException("Category with id: " + req + " is not found");
            }

        }
        List<CategoryArticle> categoryArticles = categories.stream()
                .map(category -> CategoryArticle.builder()
                        .article(article)
                        .category(category)
                        .build()).toList();

        categoryArticleRepository.saveAll(categoryArticles);


        return articleRepository.save(article).toResponse();
    }
}