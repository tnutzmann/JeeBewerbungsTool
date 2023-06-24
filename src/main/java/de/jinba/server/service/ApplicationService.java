package de.jinba.server.service;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.JobApplication;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.ApplicationStatus;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.exception.ParameterInvalidException;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.repository.JobApplicationRepository;
import de.jinba.server.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for handling job applications.
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final AppUserDetailsService appUserDetailsService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferDetailsService jobOfferDetailsService;

    /**
     * Applies user to job offer.
     *
     * @param user    User that is applying.
     * @param offerId Id of job offer.
     */
    public void applyForJobOffer(AppUser user, String offerId) {
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("Job offer does not exist"));
        applyForJobOffer(user, offer);
    }

    /**
     * Applies user to job offer.
     *
     * @param user  User that is applying.
     * @param offer Job offer.
     */
    public void applyForJobOffer(AppUser user, JobOffer offer) {
        if (jobOfferDetailsService.hasUserAppliedToJobOffer(user, offer)) {
            throw new ParameterInvalidException("You have already applied to this job offer");
        }
        JobApplication application = JobApplication.builder()
                .applicant(user)
                .jobOffer(offer)
                .status(ApplicationStatus.PENDING)
                .build();
        jobApplicationRepository.save(application);
    }

    /**
     * Rejects application. Only the admin of the company that the job offer belongs to can reject applications.
     *
     * @param id      Id of application.
     * @param message Message to applicant.
     */
    public void rejectApplication(String id, String message) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application does not exist"));
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("You are not authorized to reject this application"));
        if (!application.getJobOffer().getCompany().getAdmin().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to accept this application");
        }
        application.setStatus(ApplicationStatus.REJECTED);
        application.setAnswerMessage(message);
        jobApplicationRepository.save(application);
    }

    /**
     * Accepts application. Only the admin of the company that the job offer belongs to can accept applications.
     *
     * @param id      Id of application.
     * @param message Message to applicant.
     */
    public void acceptApplication(String id, String message) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application does not exist"));
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("You are not authorized to reject this application"));
        if (!application.getJobOffer().getCompany().getAdmin().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to accept this application");
        }
        application.setStatus(ApplicationStatus.ACCEPTED);
        application.setAnswerMessage(message);
        jobApplicationRepository.save(application);
    }
}
