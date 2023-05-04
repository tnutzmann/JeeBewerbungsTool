package de.jinba.server.repository;

import de.jinba.server.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
}
