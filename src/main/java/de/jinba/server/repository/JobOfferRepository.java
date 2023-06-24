package de.jinba.server.repository;

import de.jinba.server.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This repository provides DB operations for {@link JobOffer}.
 */
@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, String> {
    /**
     * This method finds all {@link JobOffer}s by a given Company, but only those that a given User has not applied to.
     * @param userId The id of the {@link de.jinba.server.entity.AppUser}.
     * @param companyId The id of the {@link de.jinba.server.entity.Company}.
     * @return A list of {@link JobOffer}s that a given User has not applied to.
     */
    @Query("""
    SELECT j FROM JobOffer j
    WHERE j.company.id = ?2
      AND j.id NOT IN (SELECT job.id FROM JobOffer job
                                     INNER JOIN JobApplication app ON app.jobOffer.id = job.id
                                     WHERE app.applicant.id = ?1)
    """)
    List<JobOffer> findAllUnappliedJobOffersByCompanyAndUser(String userId, String companyId);

}
