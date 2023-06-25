package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobApplication;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.ApplicationStatus;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * This controller handles the display of job offers.
 */
@Controller
@RequiredArgsConstructor
public class JobOfferDisplayController {
    private final CompanyDetailsService companyDetailsService;
    private final JobOfferDetailsService jobOfferDetailsService;
    private final AppUserDetailsService appUserDetailsService;

    /**
     * Shows the job offers a company has with a given id.
     *
     * @param model the model.
     * @param id    the id of the company.
     * @return the template.
     */
    @GetMapping("/company/{id}/offer")
    public String viewCompanyOffersPage(Model model, @PathVariable String id) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized user tried to access company offers page."));
        Company company = companyDetailsService.getById(id);
        // Check if the user is the admin of the company.
        boolean isOwnCompany = company.getAdmin().getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        model.addAttribute("company", company);
        model.addAttribute("isOwnCompany", isOwnCompany);
        // If the user is a default user, add the ids of the job offers the user has not applied to.
        if (currentUser.getRole() == Role.DEFAULT_USER) {
            model.addAttribute("unappliedJobIds", jobOfferDetailsService.getAllUnappliedJobOffersByCompany(currentUser, company)
                    .stream()
                    .map(JobOffer::getId)
                    .toList());
        }
        return "company-offers";
    }

    /**
     * Shows the job offer with the given id.
     *
     * @param model the model.
     * @param id    the id of the job offer.
     * @return the template.
     */
    @GetMapping("/offer/{id}")
    public String viewJobOfferPage(Model model,
                                   @PathVariable String id) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized user tried to access job offer page."));
        JobOffer jobOffer = jobOfferDetailsService.getById(id);
        Company company = jobOffer.getCompany();
        boolean isOwnCompany = company.getAdmin().getUsername().equals(currentUser.getUsername());
        // If the user is a default user, check if the user has applied to the job offer.
        if (currentUser.getRole().equals(Role.DEFAULT_USER)) {
            Optional<JobApplication> application = jobOffer.getApplications()
                    .stream()
                    .filter(a -> a.getApplicant().getUsername().equals(currentUser.getUsername()))
                    .findFirst();
            model.addAttribute("hasApplied", application.isPresent());
            application.ifPresent(jobApplication -> model.addAttribute("jobApplication", jobApplication));
        }
        if (currentUser.getRole().equals(Role.COMPANY_USER)) {
            model.addAttribute("hasOpenApplications", jobOffer.getApplications()
                    .stream()
                    .anyMatch(a -> a.getStatus().equals(ApplicationStatus.PENDING)));
            model.addAttribute("hasAcceptedApplications", jobOffer.getApplications()
                    .stream()
                    .anyMatch(a -> a.getStatus().equals(ApplicationStatus.ACCEPTED)));
            model.addAttribute("hasRejectedApplications", jobOffer.getApplications()
                    .stream()
                    .anyMatch(a -> a.getStatus().equals(ApplicationStatus.REJECTED)));
        }
        model.addAttribute("jobOffer", jobOffer);
        model.addAttribute("isOwnCompany", isOwnCompany);
        model.addAttribute("company", company);
        return "joboffer";
    }
}
