package kshrd.group2.article_mgmt.model.dto.request;

import kshrd.group2.article_mgmt.model.entity.AppUser;
import kshrd.group2.article_mgmt.model.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserRequest {
    private String username;
    private String email;
    private UserRole role;
    private String password;
    private String address;
    private String phoneNumber;

    public AppUser toEntity() {
        return AppUser.builder()
                .appUsername(username)
                .email(email)
                .role(role)
                .password(password)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
