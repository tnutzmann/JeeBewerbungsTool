package de.jinba.server.service;

import de.jinba.server.dto.JobOfferCreationRequest;
import de.jinba.server.dto.JobOfferEditRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.JobOfferSkill;
import de.jinba.server.entity.enumuration.JobOfferStatus;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.repository.JobOfferRepository;
import de.jinba.server.repository.JobOfferSkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for handling job offers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferSkillRepository jobOfferSkillRepository;
    private final SkillService skillService;

    /**
     * Adds new job offer.
     *
     * @param company Company that adds job offer.
     * @param request Request containing job offer data.
     * @return Created job offer.
     */
    @Transactional
    public JobOffer addNewJobOffer(Company company, JobOfferCreationRequest request) {
        JobOffer jobOffer = JobOffer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .company(company)
                .status(JobOfferStatus.OPEN)
                .build();

        JobOffer savedOffer = jobOfferRepository.save(jobOffer);

        List<JobOfferSkill> skills = request.getSkills()
                .stream()
                .map(s -> JobOfferSkill.builder()
                        .skill(skillService.getSkillAndCreateIfNotExists(s.getName()))
                        .level(s.getLevel())
                        .jobOffer(savedOffer)
                        .build())
                .toList();
        jobOfferSkillRepository.saveAll(skills);

        return savedOffer;
    }

    /**
     * Updates job offer.
     *
     * @param jobOffer Job offer to update.
     * @param request  Request containing job offer data.
     * @return Updated job offer.
     */
    public JobOffer updateJobOffer(JobOffer jobOffer, JobOfferEditRequest request) {
        jobOffer.setTitle(request.getTitle());
        jobOffer.setDescription(request.getDescription());
        jobOffer.setLocation(request.getLocation());

        return jobOfferRepository.save(jobOffer);
    }

    /**
     * Gets a job offer by id.
     *
     * @param id ID of job offer.
     * @return Job offer.
     */
    public JobOffer getById(String id) {
        return jobOfferRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Could not find JobOffer with id: %s", id)));
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
     * @param companyId ID of company.
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
     * Finds job offers that are suggested for a user.
     * @param user The {@link AppUser} that is requesting the job offers.
     * @return List of job offers.
     */
    public List<JobOffer> findJobOfferSuggestionsByUser(AppUser user) {
        return jobOfferRepository.findSuggestionsByUserId(user.getId());
    }

    /**
     * Finds job offers by the given query string.
     * @param user The {@link AppUser} that is requesting the job offers.
     * @param queryString The query string.
     * @return List of job offers.
     */
    public List<JobOffer> findJobOfferByQueryString(AppUser user,  String queryString) {
        String sqlQueryString = String.format("%%%s%%", queryString);
        return jobOfferRepository.findSearchResults(sqlQueryString, user.getId());
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
