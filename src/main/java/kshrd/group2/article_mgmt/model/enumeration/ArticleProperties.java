package kshrd.group2.article_mgmt.model.enumeration;

import lombok.Getter;

@Getter
public enum ArticleProperties {
    ARTICLE_ID("articleId"),
    TITLE("title"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ArticleProperties(String fieldName) {
        this.fieldName = fieldName;
    }
}