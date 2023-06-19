package de.jinba.server.entity;

import de.jinba.server.entity.enumuration.SkillLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobOfferSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "job_offer")
    private JobOffer jobOffer;
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
    @Enumerated(EnumType.ORDINAL)
    private SkillLevel level;
}
