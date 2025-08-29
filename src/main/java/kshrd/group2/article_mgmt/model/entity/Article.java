package kshrd.group2.article_mgmt.model.entity;

import jakarta.persistence.*;
import kshrd.group2.article_mgmt.model.dto.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "articles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article extends BaseEntityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private AppUser user;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryArticle> categoryArticles;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    public ArticleResponse toResponse() {
        return ArticleResponse.builder()
                .articleId(articleId)
                .title(title.trim())
                .description(description.trim())
                .userId(user.getUserId())
                .categories(categoryArticles.stream()
                        .map(ca -> ca.getCategory().getCategoryName())
                        .toList())
                .createdAt(getCreatedAt())
                .editedAt(getEditedAt())
                .build();
    }
}