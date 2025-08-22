package kshrd.group2.article_mgmt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kshrd.group2.article_mgmt.model.dto.response.ApiResponse;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.service.AppUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RequestMapping("/api/v1/test")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TestController extends BaseController {
    private final AppUserService appUserService;

    @GetMapping("/current-profile")
    public ResponseEntity<ApiResponse<AppUserResponse>> getCurrentProfile() {
        return responseEntity("Fetch current profile successfully", HttpStatus.OK, appUserService.getCurrentProfile());
    }
    
}
