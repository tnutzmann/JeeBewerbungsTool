package de.jinba.server.service;

import de.jinba.server.dto.JobOfferCreationRequest;
import de.jinba.server.dto.JobOfferEditRequest;
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
    private final CompanyDetailsService companyDetailsService;
    private final SkillService skillService;
    private final AppUserDetailsService appUserDetailsService;

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

        JobOffer savedJobOffer = jobOfferRepository.save(jobOffer);

        return savedJobOffer;
    }

    /**
     * Gets a job offer by id.
     *
     * @param id Id of job offer.
     * @return Job offer.
     */
    public JobOffer getById(String id) {
        return jobOfferRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Could not find JobOffer with id: %s", id)));
    }
}
