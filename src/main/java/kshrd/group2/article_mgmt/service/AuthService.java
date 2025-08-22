package kshrd.group2.article_mgmt.service;

import kshrd.group2.article_mgmt.model.dto.request.AppUserRequest;
import kshrd.group2.article_mgmt.model.dto.request.AuthRequest;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.model.dto.response.AuthResponse;

public interface AuthService {

    AppUserResponse register(AppUserRequest request);

    AuthResponse login(AuthRequest request);

}
