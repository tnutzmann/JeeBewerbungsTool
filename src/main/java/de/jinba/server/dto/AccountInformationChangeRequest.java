package de.jinba.server.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountInformationChangeRequest implements Serializable {
    @NotEmpty(message = "Cannot be empty")
    private String firstName;
    @NotEmpty(message = "Cannot be empty")
    private String lastName;
    @NotEmpty(message = "Cannot be empty")
    private String details;
}
