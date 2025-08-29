package kshrd.group2.article_mgmt.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.model.dto.request.UpdateAppUserRequest;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.repository.AppUserRepository;
import kshrd.group2.article_mgmt.service.AppUserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser foundAppUser = appUserRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User with email: '" + username + "' doesn't exist"));

        return foundAppUser;
    }

    @Override
    public AppUserResponse getCurrentProfile() {
        return getCurrentUser().toResponse();
    }

    @Override
    public AppUserResponse updateCurrentUser(UpdateAppUserRequest request) {
        AppUser foundUser = appUserRepository.findByEmail(getCurrentUser().getEmail())
                .orElseThrow(() -> new NotFoundException("User doesn't exist"));

        foundUser.setAddress(request.getAddress().trim());
        foundUser.setPhoneNumber(request.getPhoneNumber().trim());

        return appUserRepository.save(foundUser).toResponse();
    }
}
