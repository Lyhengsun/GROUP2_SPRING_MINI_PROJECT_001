package kshrd.group2.article_mgmt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import kshrd.group2.article_mgmt.model.dto.request.AppUserRequest;
import kshrd.group2.article_mgmt.model.dto.request.AuthRequest;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.model.dto.response.AuthResponse;
import kshrd.group2.article_mgmt.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        return responseEntity("Login successfully", HttpStatus.CREATED, authService.login(request));
    }

    @PostMapping("/register")
    ResponseEntity<ApiResponse<AppUserResponse>> register(@RequestBody AppUserRequest request) {
        return responseEntity("Register successfully", HttpStatus.CREATED, authService.register(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestParam String refreshToken) {
        return responseEntity("Token refreshed successfully", authService.refreshToken(refreshToken));
    }
    
}