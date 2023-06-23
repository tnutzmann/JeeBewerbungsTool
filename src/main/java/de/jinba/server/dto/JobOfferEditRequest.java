package de.jinba.server.dto;

import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.JobOfferSkill;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobOfferEditRequest implements Serializable {
    @NotEmpty(message = "Cannot be empty")
    private String id;
    @NotEmpty(message = "Cannot be empty")
    private String title;
    @NotEmpty(message = "Cannot be empty")
    private String description;
    @NotEmpty(message = "Cannot be empty")
    private String location;
    private List<JobOfferSkillDto> skills;

    public JobOfferEditRequest(JobOffer jobOffer) {
        this.id = jobOffer.getId();
        this.title = jobOffer.getTitle();
        this.description = jobOffer.getDescription();
        this.location = jobOffer.getLocation();

        this.skills = new ArrayList<>();
        for(JobOfferSkill jos : jobOffer.getSkills()) {
            JobOfferSkillDto josDto = new JobOfferSkillDto(jos.getSkill().getName(), jos.getLevel());
            this.skills.add(josDto);
        }
    }
}
