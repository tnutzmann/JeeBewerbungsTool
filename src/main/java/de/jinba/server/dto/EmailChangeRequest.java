package de.jinba.server.dto;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailChangeRequest implements Serializable {
    @NotEmpty(message = "Cannot be empty")
    @Email
    private String email;
}
