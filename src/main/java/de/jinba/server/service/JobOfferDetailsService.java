package de.jinba.server.service;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling job offer details.
 * TODO: Merge with JobOfferService.
 */
@Service
@RequiredArgsConstructor
public class JobOfferDetailsService {
    private final JobOfferRepository jobOfferRepository;

    /**
     * Gets job offer by id.
     *
     * @param id Id of job offer.
     * @return Job offer.
     */
    public JobOffer getById(String id) {
        return jobOfferRepository.findById(id).orElseThrow();
    }

    /**
     * Gets all job offers.
     *
     * @return List of job offers.
     */
    public List<JobOffer> getAll() {
        return jobOfferRepository.findAll();
    }

    /**
     * Gets all job offers by company that the user has not yet applied to.
     *
     * @param user      User that is requesting job offers.
     * @param companyId Id of company.
     * @return List of job offers that the user has not yet applied to.
     */
    public List<JobOffer> getAllUnappliedJobOffersByCompany(AppUser user, String companyId) {
        return jobOfferRepository.findAllUnappliedJobOffersByCompanyAndUser(user.getId(), companyId);
    }

    /**
     * Gets all job offers by company that the user has not yet applied to.
     *
     * @param user    User that is requesting job offers.
     * @param company the company.
     * @return List of job offers that the user has not yet applied to.
     */
    public List<JobOffer> getAllUnappliedJobOffersByCompany(AppUser user, Company company) {
        return this.getAllUnappliedJobOffersByCompany(user, company.getId());
    }

    /**
     * Checks if user has applied to job offer.
     *
     * @param user     User that is requesting the check.
     * @param jobOffer Job offer.
     * @return True if user has applied to job offer, false otherwise.
     */
    public boolean hasUserAppliedToJobOffer(AppUser user, JobOffer jobOffer) {
        return jobOffer.getApplications()
                .stream()
                .anyMatch(application -> application.getApplicant()
                        .getId()
                        .equals(user.getId()));
    }
}
