package de.jinba.server.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDetailsChangeRequest implements Serializable {
    @NotEmpty(message = "Cannot be empty")
    private String name;
}
