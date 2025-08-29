package kshrd.group2.article_mgmt.service.impl;

import jakarta.transaction.Transactional;
import kshrd.group2.article_mgmt.exception.ForbiddenException;
import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.request.ArticleRequest;
import kshrd.group2.article_mgmt.model.dto.request.CommentRequest;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import kshrd.group2.article_mgmt.model.dto.response.CommentResponse;
import kshrd.group2.article_mgmt.model.dto.response.ArticleCommentResponse;
import kshrd.group2.article_mgmt.model.entity.*;
import kshrd.group2.article_mgmt.model.enumeration.ArticleProperties;
import kshrd.group2.article_mgmt.model.enumeration.UserRole;
import kshrd.group2.article_mgmt.repository.ArticleRepository;
import kshrd.group2.article_mgmt.repository.CategoryArticleRepository;
import kshrd.group2.article_mgmt.repository.CategoryRepository;
import kshrd.group2.article_mgmt.repository.CommentRepository;
import kshrd.group2.article_mgmt.service.ArticleService;
import kshrd.group2.article_mgmt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryArticleRepository categoryArticleRepository;
    private final CommentRepository commentRepository;
    private final CategoryService categoryService;

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
            Category category = categoryService.increaseAmountOfCategoryArticle(id);
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
        if (getCurrentUser().getRole() != UserRole.ROLE_AUTHOR &&
            !article.getUser().getUserId().equals(getCurrentUser().getUserId())) {
            throw new ForbiddenException("You don’t have permission to delete this article");
        }

        article.getCategoryArticles()
                .stream()
                .map(cat -> cat.getCategory().getCategoryId())
                .forEach(categoryService::decreaseAmountOfCategoryArticle);

        articleRepository.delete(article);
    }

    @Override
    @Transactional
    public ArticleResponse updateArticleById(Long articleId, ArticleRequest request) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article with id: " + articleId + " is not found"));

        if (!article.getUser().getUserId().equals(getCurrentUser().getUserId())) {
            throw new ForbiddenException("You don’t have permission to update this article");
        }

        article.setTitle(request.getTitle());
        article.setDescription(request.getDescription());

        List<Category> oldCategories = categoryArticleRepository.findAllByArticle(article)
                .stream()
                .map(CategoryArticle::getCategory)
                .toList();
        Set<Long> oldCategoryIds = oldCategories.stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toSet());

        List<Long> newCategoryIds = request.getCategoryIds();

        Set<Long> duplicateIds = newCategoryIds.stream()
                .filter(id -> Collections.frequency(newCategoryIds, id) > 1)
                .collect(Collectors.toSet());
        if (!duplicateIds.isEmpty()) {
            throw new BadRequestException("Duplicate category ids: " + duplicateIds);
        }

        List<Category> newCategories = categoryRepository.findAllByUserUserIdAndCategoryIdIn(
                getCurrentUser().getUserId(),
                newCategoryIds
        );

        Set<Long> foundIds = newCategories.stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toSet());
        List<Long> notFoundIds = newCategoryIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();
        if (!notFoundIds.isEmpty()) {
            throw new BadRequestException("Categories not found or not owned by current user: " + notFoundIds);
        }

        categoryArticleRepository.deleteByArticle(article);
        categoryArticleRepository.flush();

        Set<Long> newCategoryIdsSet = new HashSet<>(newCategoryIds);

        oldCategoryIds.stream()
                .filter(id -> !newCategoryIdsSet.contains(id))
                .forEach(categoryService::decreaseAmountOfCategoryArticle);

        newCategoryIdsSet.stream()
                .filter(id -> !oldCategoryIds.contains(id))
                .forEach(categoryService::increaseAmountOfCategoryArticle);

        List<CategoryArticle> categoryArticles = newCategories.stream()
                .map(category -> CategoryArticle.builder()
                        .article(article)
                        .category(category)
                        .build())
                .toList();
        categoryArticleRepository.saveAll(categoryArticles);

        return articleRepository.save(article).toResponse();
    }

    @Override
    public ArticleCommentResponse createComment(Long id, CommentRequest commentRequest) {

        // validate article id not found
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article with id: " + id + " is not found"));

        AppUser user = getCurrentUser();

        Comment comment = commentRequest.toCommentEntity();
        comment.setArticle(article);
        comment.setUser(user);
        commentRepository.save(comment);

        return ArticleCommentResponse.builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .description(article.getDescription())
                .userId(article.getUser().getUserId())
                .categories(article.getCategoryArticles().stream().map(cat -> cat.getCategory().getCategoryName()).toList())
                .createdAt(LocalDateTime.now())
                .editedAt(LocalDateTime.now())
                .commentResponses(article.getComments().stream()
                        .map(Comment::toResponse)
                        .sorted(Comparator.comparing(CommentResponse::getCommentId).reversed())
                        .toList())
                .build();
    }

    @Override
    public ArticleCommentResponse getAllCommentByArticleId(Long id) {

        // validate article id not found
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article with id: " + id + " is not found"));

        return ArticleCommentResponse.builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .description(article.getDescription())
                .userId(article.getUser().getUserId())
                .categories(article.getCategoryArticles().stream().map(cat -> cat.getCategory().getCategoryName()).toList())
                .createdAt(LocalDateTime.now())
                .editedAt(LocalDateTime.now())
                .commentResponses(article.getComments().stream()
                        .map(Comment::toResponse)
                        .sorted(Comparator.comparing(CommentResponse::getCommentId).reversed())
                        .toList())
                .build();
    }
}