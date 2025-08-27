package kshrd.group2.article_mgmt.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kshrd.group2.article_mgmt.model.dto.response.BookmarkResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "article_id", "user_id" })
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bookmark extends BaseEntityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id", referencedColumnName = "article_id")
    private Article article;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private AppUser user;

    public BookmarkResponse toResponse() {
        return BookmarkResponse.builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .description(article.getDescription())
                .userId(article.getUser().getUserId())
                .createdAt(getCreatedAt())
                .editedAt(getEditedAt())
                .build();
    }
}