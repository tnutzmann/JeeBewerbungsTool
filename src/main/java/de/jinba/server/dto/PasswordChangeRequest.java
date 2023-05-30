package de.jinba.server.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordChangeRequest implements Serializable {
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9]).*$", message = "Must contain at least one digit")
    @Pattern(regexp = "^(?=.*[a-z]).*$", message = "Must contain at least one lower case letter")
    @Pattern(regexp = "^(?=.*[A-Z]).*$", message = "Must contain at least one upper case letter")
    @Pattern(regexp = "^(?=.*[@#$%^&+=!]).*$", message = "Must contain at least one special character (@#$%^&+=!)")
    @Pattern(regexp = "^(?=\\S+$).*$", message = "No whitespace allowed")
    private String newPassword;
    @NotEmpty(message = "Cannot be empty")
    private String currentPassword;
    @NotEmpty(message = "Cannot be empty")
    private String confirmPassword;
}