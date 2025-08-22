package kshrd.group2.article_mgmt.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;

public interface AppUserService extends UserDetailsService {

    AppUserResponse getCurrentProfile();
    
}
