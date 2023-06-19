package de.jinba.server.dto;

import de.jinba.server.entity.enumuration.SkillLevel;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobOfferCreationRequest implements Serializable {
    @NotEmpty(message = "Cannot be empty")
    private String title;
    @NotEmpty(message = "Cannot be empty")
    private String description;
    @NotEmpty(message = "Cannot be empty")
    private String location;
    private List<JobOfferSkillDto> skills;
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class JobOfferSkillDto implements Serializable {
        private String name;
        private SkillLevel level;
    }
}
