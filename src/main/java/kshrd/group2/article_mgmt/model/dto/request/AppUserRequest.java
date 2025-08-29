package kshrd.group2.article_mgmt.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 100, message = "Username need to be between 8 and 100 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 255, message = "Email can't be longer than 255 characters")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role is required")
    private UserRole role;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
    private String password;

    @NotBlank(message = "Address is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\s,.'\\-#]{5,100}$", message = "Invalid address")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+855[1-9][0-9]{7,8}$")
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
