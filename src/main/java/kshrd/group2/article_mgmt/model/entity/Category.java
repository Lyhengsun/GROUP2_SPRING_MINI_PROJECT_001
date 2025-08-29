package kshrd.group2.article_mgmt.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kshrd.group2.article_mgmt.model.dto.response.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 100, nullable = false)
    private String categoryName;

    @Column(name = "amount_of_article", nullable = false)
    private Integer amountOfArticle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private AppUser user;

    @PrePersist
    private void prePersist() {
        if (amountOfArticle == null) {
            amountOfArticle = 0;
        }
    }

    public CategoryResponse toResponse() {
        return CategoryResponse.builder()
                .categoryId(this.categoryId)
                .categoryName(this.categoryName)
                .amountOfArticles(this.amountOfArticle)
                .createdAt(this.getCreatedAt())
                .editedAt(this.getEditedAt())
                .build();
    }
}
