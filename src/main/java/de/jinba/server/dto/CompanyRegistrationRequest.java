package de.jinba.server.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyRegistrationRequest implements Serializable {
    @Valid
    private UserRegistrationRequest user;
    @Valid
    private CompanyDetails company;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CompanyDetails implements Serializable{
        @NotEmpty(message = "Cannot be empty")
        private String name;
        @NotEmpty(message = "Cannot be empty")
        private String description;
    }
}
