package de.jinba.server.dto;

import de.jinba.server.entity.enumuration.SkillLevel;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobOfferSkillDto implements Serializable {
    private String name;
    private SkillLevel level;
}
