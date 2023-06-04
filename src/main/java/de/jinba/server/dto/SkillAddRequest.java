package de.jinba.server.dto;

import de.jinba.server.entity.enumuration.SkillLevel;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillAddRequest implements Serializable {
    @NotEmpty(message = "Cannot be empty")
    private String skillName;
    private SkillLevel level;
}
