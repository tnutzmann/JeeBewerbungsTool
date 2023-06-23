package de.jinba.server.repository;

import de.jinba.server.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, String> {
    @Query("""
    SELECT j FROM JobOffer j
    WHERE j.company.id = ?2
      AND j.id NOT IN (SELECT job.id FROM JobOffer job
                                     INNER JOIN JobApplication app ON app.jobOffer.id = job.id
                                     WHERE app.applicant.id = ?1)
    """)
    List<JobOffer> findAllUnappliedJobOffersByCompanyAndUser(String userId, String companyId);

}
