package kshrd.group2.article_mgmt.model.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kshrd.group2.article_mgmt.model.dto.response.AppUserResponse;
import kshrd.group2.article_mgmt.model.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser extends BaseEntityAudit implements UserDetails {
    @Id
    @GeneratedValue
    private Long userId;

    @Column(name = "username", nullable = false, length = 100)
    private String appUsername;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(nullable = true, length = 100)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public AppUserResponse toResponse() {
        return AppUserResponse.builder()
                .userId(userId)
                .appUsername(appUsername)
                .email(email)
                .role(role)
                .address(address)
                .phoneNumber(phoneNumber)
                .createdAt(getCreatedAt())
                .editedAt(getEditedAt()).build();
    }
}
