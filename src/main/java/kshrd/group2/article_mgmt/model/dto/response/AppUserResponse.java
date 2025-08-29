package kshrd.group2.article_mgmt.model.dto.response;

import java.time.LocalDateTime;

import kshrd.group2.article_mgmt.model.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AppUserResponse {
    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private String address;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
