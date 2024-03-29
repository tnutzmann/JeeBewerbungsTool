package de.jinba.server.entity;

import de.jinba.server.entity.enumuration.JobOfferStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This entity represents a job offer.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String location;
    @OneToMany(mappedBy = "jobOffer")
    private List<JobOfferSkill> skills;

    @OneToMany(mappedBy = "jobOffer")
    private List<JobApplication> applications;
    @ManyToOne
    private Company company;

    @Enumerated(EnumType.ORDINAL)
    private JobOfferStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
}
