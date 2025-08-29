package kshrd.group2.article_mgmt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UpdateAppUserRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+855[1-9][0-9]{7,8}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\s,.'\\-#]{5,100}$", message = "Invalid address")
    private String address;
}
