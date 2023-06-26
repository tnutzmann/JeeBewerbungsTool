package de.jinba.server.repository;

import de.jinba.server.entity.JobOffer;
import org.springframework.data.domain.Page;
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

    /**
     * This method gives a list of {@link JobOffer}'s that fit the given {@link de.jinba.server.entity.AppUser}'s skills.
     * Offers that the user has already applied to are excluded.
     * @param userId the id of the {@link de.jinba.server.entity.AppUser}.
     * @return A list of {@link JobOffer}'s that fit the given {@link de.jinba.server.entity.AppUser}'s skills.
     */
    @Query("""
            select jo
            from JobOfferSkill jobSkill
            inner join JobOffer jo on jobSkill.jobOffer.id = jo.id
            where jobSkill.skill.id in (select s.id from Skill s
                                        inner join AppUserSkill userSkill on userSkill.skill.id = s.id
                                        where userSkill.appUser.id = ?1)
            and jo.id not in (select app.jobOffer.id
                              from JobApplication app
                              where app.applicant.id = ?1)
            group by jo.id
            order by count(jobSkill.skill.id) desc
            limit 10
    """)
    List<JobOffer> findSuggestionsByUserId(String userId);

    /**
     * This method finds all {@link JobOffer}s that a given {@link de.jinba.server.entity.AppUser}
     * has not applied to and matches the given query.
     *
     * @param query The query to match.
     * @param userId The id of the {@link de.jinba.server.entity.AppUser}.
     * @return A list of {@link JobOffer}s that a given {@link de.jinba.server.entity.AppUser} matches
     */
    @Query("""
            select jo
            from JobOffer jo
            where jo.id not in (select app.jobOffer.id
                                  from JobApplication app
                                  where app.applicant.id = ?2)
                 and (jo.title like ?1
                 or jo.description like ?1
                    or jo.location like ?1
                    or jo.id in (select jobSkill.jobOffer.id
                                  from JobOfferSkill jobSkill
                                  where jobSkill.skill.id in (select s.id
                                                              from Skill s
                                                              where s.name like ?1)))
    """)
    List<JobOffer> findSearchResults(String query, String userId);
}
