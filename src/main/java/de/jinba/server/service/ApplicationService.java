package de.jinba.server.service;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.JobApplication;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.ApplicationStatus;
import de.jinba.server.repository.JobApplicationRepository;
import de.jinba.server.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferRepository jobOfferRepository;

    public void applyForJobOffer(AppUser user, String offerId){
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow();
        applyForJobOffer(user, offer);
    }

    public void applyForJobOffer(AppUser user, JobOffer offer){
        JobApplication application = JobApplication.builder()
                .applicant(user)
                .jobOffer(offer)
                .status(ApplicationStatus.PENDING)
                .build();
        jobApplicationRepository.save(application);
    }
}
