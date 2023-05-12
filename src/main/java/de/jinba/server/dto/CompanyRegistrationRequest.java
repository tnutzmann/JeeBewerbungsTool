package de.jinba.server.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyRegistrationRequest {
    @Valid
    private UserRegistrationRequest user;
    @Valid
    private CompanyDetails company;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CompanyDetails {
        @NotEmpty(message = "Cannot be empty")
        private String name;
        @NotEmpty(message = "Cannot be empty")
        private String description;
    }
}
