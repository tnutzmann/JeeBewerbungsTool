package de.jinba.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationRequest {
    @NotEmpty(message = "Cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotEmpty(message = "Cannot be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9]).*$", message = "Must contain at least one digit")
    @Pattern(regexp = "^(?=.*[a-z]).*$", message = "Must contain at least one lower case letter")
    @Pattern(regexp = "^(?=.*[A-Z]).*$", message = "Must contain at least one upper case letter")
    @Pattern(regexp = "^(?=.*[@#$%^&+=!]).*$", message = "Must contain at least one special character (@#$%^&+=!)")
    @Pattern(regexp = "^(?=\\S+$).*$", message = "No whitespace allowed")
    private String password;
    @NotEmpty(message = "Cannot be empty")
    private String firstName;
    @NotEmpty(message = "Cannot be empty")
    private String lastName;
    @NotEmpty(message = "Cannot be empty")
    private String details;
}
