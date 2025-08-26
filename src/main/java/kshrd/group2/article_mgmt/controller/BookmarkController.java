package kshrd.group2.article_mgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.entity.Bookmark;
import kshrd.group2.article_mgmt.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@Tag(name = "Bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;


    @Operation(summary = "Get all articles which has added bookmark by current user id")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Bookmark>>> getAllBookmarks(
            @RequestParam(defaultValue = "1") @Valid @Min(value = 1, message = "Page must be greater than 0") Integer page,
            @RequestParam(defaultValue = "10") @Valid @Min(value = 1, message = "Size must be greater than 0") Integer size,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        List<Bookmark> bookmarks = bookmarkService.getAllBookmarks(page, size, sortDirection);
        ApiResponse<List<Bookmark>> response = ApiResponse.<List<Bookmark>>builder()
                .success(true)
                .message("Get all articles which has added bookmark successfully")
                .payload(bookmarks)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add bookmark on article")
    @PostMapping("/{articleId}")
    public ResponseEntity<ApiResponse<Bookmark>> createBookmark(@PathVariable("articleId") Long articleId) {
        Bookmark bookmark = bookmarkService.createBookmark(articleId);
        ApiResponse<Bookmark> response = ApiResponse.<Bookmark>builder()
                .success(true)
                .message("Added bookmark successfully")
                .payload(bookmark)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Delete bookmark on article")
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<String>> deleteBookmark(@PathVariable("articleId") Long articleId) {
        bookmarkService.deleteBookmark(articleId);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Deleted bookmark successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
