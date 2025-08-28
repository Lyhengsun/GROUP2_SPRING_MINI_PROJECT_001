package kshrd.group2.article_mgmt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.BookmarkResponse;
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
@Tag(name = "Bookmark Controller")
@SecurityRequirement(name = "bearerAuth")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "Get all articles which has added bookmark by current user id")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookmarkResponse>>> getAllBookmarks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        try {

            if (page <= 0) throw new BadRequestException("Page index must not be less than zero");
            if (size <= 0) throw new BadRequestException("Page size must not be less than one");

            List<BookmarkResponse> bookmarks = bookmarkService.getAllBookmarks(page, size, sortDirection);
            ApiResponse<List<BookmarkResponse>> response = ApiResponse.<List<BookmarkResponse>>builder()
                    .success(true)
                    .message("Get all articles which has added bookmark successfully")
                    .payload(bookmarks)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Operation(summary = "Add bookmark on article")
    @PostMapping("/{articleId}")
    public ResponseEntity<ApiResponse<BookmarkResponse>> addBookmark(@PathVariable("articleId") Long articleId) {
        try {
            BookmarkResponse bookmark = bookmarkService.addBookmark(articleId);
            ApiResponse<BookmarkResponse> response = ApiResponse.<BookmarkResponse>builder()
                    .success(true)
                    .message("Added bookmark successfully")
                    .payload(bookmark)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Operation(summary = "Delete bookmark on article")
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(@PathVariable("articleId") Long articleId) {
        try {
            bookmarkService.deleteBookmark(articleId);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Deleted bookmark successfully")
                    .payload(null)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
