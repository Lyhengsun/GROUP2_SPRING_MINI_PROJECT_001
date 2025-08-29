package kshrd.group2.article_mgmt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kshrd.group2.article_mgmt.model.dto.request.UpdateAppUserRequest;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller")
@SecurityRequirement(name = "bearerAuth")
public class UserController extends BaseController {
    private final AppUserService appUserService;

    @GetMapping
    @Operation(summary = "Get current user. can be used by all roles")
    public ResponseEntity<ApiResponse<AppUserResponse>> getCurrentUser() {
        return responseEntity("Get current user successfully", appUserService.getCurrentProfile());
    }

    @PutMapping
    @Operation(summary = "Update current user. can be used by all roles")
    public ResponseEntity<ApiResponse<AppUserResponse>> updateCurrentUser (@RequestBody UpdateAppUserRequest request) {
        return responseEntity("Updated Current User successfully", appUserService.updateCurrentUser(request));
    }
}
