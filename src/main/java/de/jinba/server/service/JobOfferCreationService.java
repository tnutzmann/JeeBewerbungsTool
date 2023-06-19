package de.jinba.server.service;

import de.jinba.server.dto.JobOfferCreationRequest;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.JobOfferSkill;
import de.jinba.server.entity.enumuration.JobOfferStatus;
import de.jinba.server.repository.JobOfferRepository;
import de.jinba.server.repository.JobOfferSkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobOfferCreationService {
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferSkillRepository jobOfferSkillRepository;
    private final CompanyDetailsService companyDetailsService;
    private final SkillService skillService;
    @Transactional
    public JobOffer addNewJobOffer(JobOfferCreationRequest request){
        Company company = companyDetailsService.findCompanyOfCurrentUser();

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
}
