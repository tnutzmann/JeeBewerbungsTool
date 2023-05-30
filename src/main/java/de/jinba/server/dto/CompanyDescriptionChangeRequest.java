package de.jinba.server.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDescriptionChangeRequest implements Serializable {
    private String description;
}
