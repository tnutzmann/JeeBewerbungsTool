package de.jinba.server.service;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.JobApplication;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.ApplicationStatus;
import de.jinba.server.exception.ParameterInvalidException;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.repository.JobApplicationRepository;
import de.jinba.server.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final AppUserDetailsService appUserDetailsService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferDetailsService jobOfferDetailsService;

    public void applyForJobOffer(AppUser user, String offerId){
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow();
        applyForJobOffer(user, offer);
    }

    public void applyForJobOffer(AppUser user, JobOffer offer){
        if(jobOfferDetailsService.hasUserAppliedToJobOffer(user, offer)){
            throw new ParameterInvalidException("You have already applied to this job offer");
        }
        JobApplication application = JobApplication.builder()
                .applicant(user)
                .jobOffer(offer)
                .status(ApplicationStatus.PENDING)
                .build();
        jobApplicationRepository.save(application);
    }

    public void rejectApplication(String id, String message) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow();
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        if(!application.getJobOffer().getCompany().getAdmin().getId().equals(currentUser.getId())){
            throw new UnauthorizedException("You are not authorized to accept this application");
        }
        application.setStatus(ApplicationStatus.REJECTED);
        application.setAnswerMessage(message);
        jobApplicationRepository.save(application);
    }

    public void acceptApplication(String id, String message) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow();
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        if(!application.getJobOffer().getCompany().getAdmin().getId().equals(currentUser.getId())){
            throw new UnauthorizedException("You are not authorized to accept this application");
        }
        application.setStatus(ApplicationStatus.ACCEPTED);
        application.setAnswerMessage(message);
        jobApplicationRepository.save(application);
    }
}
