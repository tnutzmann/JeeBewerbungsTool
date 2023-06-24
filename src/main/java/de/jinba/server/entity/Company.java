package de.jinba.server.entity;

import de.jinba.server.entity.constraint.CompanyAdminConstraint;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * This entity represents a company.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @OneToOne
    @CompanyAdminConstraint
    private AppUser admin;
    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "company_id")
    private List<JobOffer> jobOffers;
}
