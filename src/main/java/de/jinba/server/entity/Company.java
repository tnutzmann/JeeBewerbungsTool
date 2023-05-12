package de.jinba.server.entity;

import de.jinba.server.entity.constraint.CompanyAdminConstraint;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @CompanyAdminConstraint
    private AppUser admin;
    @OneToMany
    @JoinColumn(name = "job_offer_company", referencedColumnName = "id")
    private List<JobOffer> jobOffers;
}
