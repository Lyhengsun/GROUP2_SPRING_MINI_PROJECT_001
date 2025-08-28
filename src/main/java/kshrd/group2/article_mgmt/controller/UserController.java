package kshrd.group2.article_mgmt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kshrd.group2.article_mgmt.model.dto.request.UpdateAppUserRequest;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController extends BaseController {
    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<ApiResponse<AppUserResponse>> getCurrentUser() {
        return responseEntity("Get current user successfully", appUserService.getCurrentProfile());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AppUserResponse>> updateCurrentUser (@RequestBody UpdateAppUserRequest request) {
        return responseEntity("Updated Current User successfully", appUserService.updateCurrentUser(request));
    }
}
