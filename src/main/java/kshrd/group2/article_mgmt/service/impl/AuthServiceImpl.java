package kshrd.group2.article_mgmt.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kshrd.group2.article_mgmt.exception.BadRequestException;
import kshrd.group2.article_mgmt.exception.NotFoundException;
import kshrd.group2.article_mgmt.jwt.JwtService;
import kshrd.group2.article_mgmt.model.dto.request.AppUserRequest;
import kshrd.group2.article_mgmt.model.dto.request.AuthRequest;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.model.dto.response.AuthResponse;
import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.repository.AppUserRepository;
import kshrd.group2.article_mgmt.service.AppUserService;
import kshrd.group2.article_mgmt.service.AuthService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JwtService jwtService;

    private void authenticate(String email, String password) {
        try {
            AppUser appUser = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Invalid email"));

            if (!passwordEncoder.matches(password, appUser.getPassword())) {
                throw new NotFoundException("Invalid Password");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        } catch (DisabledException e) {
            throw new BadRequestException("USER_DISABLED" + e.getMessage());
        } catch (BadCredentialsException e) {
            throw new BadRequestException("INVALID_CREDENTIALS" + e.getMessage());
        }
    }

    @Override
    public AppUserResponse register(AppUserRequest request) {
        appUserRepository.findByEmail(request.getEmail()).ifPresent((u) -> {
            throw new BadRequestException("User already exist");
        });

        AppUser appUser = request.toEntity();
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        return appUserRepository.save(appUser).toResponse();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User is not registered"));

        authenticate(request.getEmail(), request.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(request.getEmail());
        final String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }

}
