package kshrd.group2.article_mgmt.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryProperty {
    CATEGORYID("categoryId"), CATEGORYNAME("categoryName"), CREATEDAT("createdAt");
    private final String fieldName;
}
