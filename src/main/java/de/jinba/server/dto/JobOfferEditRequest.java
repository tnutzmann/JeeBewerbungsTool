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

    public JobOfferEditRequest(JobOffer jobOffer) {
        this.id = jobOffer.getId();
        this.title = jobOffer.getTitle();
        this.description = jobOffer.getDescription();
        this.location = jobOffer.getLocation();
    }
}
