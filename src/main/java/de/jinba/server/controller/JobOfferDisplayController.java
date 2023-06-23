package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobApplication;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.Role;
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

@Controller
@RequiredArgsConstructor
public class JobOfferDisplayController {
    private final CompanyDetailsService companyDetailsService;
    private final JobOfferDetailsService jobOfferDetailsService;
    private final AppUserDetailsService appUserDetailsService;
    @GetMapping("/company/{id}/offer")
    public String viewCompanyOffersPage(Model model, @PathVariable String id){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        Company company = companyDetailsService.getById(id);
        boolean isOwnCompany = company.getAdmin().getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        model.addAttribute("company", company);
        model.addAttribute("isOwnCompany", isOwnCompany);
        model.addAttribute("unappliedJobIds", jobOfferDetailsService.getAllUnappliedJobOffersByCompany(currentUser, company)
                .stream()
                .map(JobOffer::getId)
                .toList());
        return "company-offers";
    }

    @GetMapping("/offer/{id}")
    public String viewJobOfferPage(Model model,
                                   @PathVariable String id){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        JobOffer jobOffer = jobOfferDetailsService.getById(id);
        Company company = jobOffer.getCompany();
        boolean isOwnCompany = company.getAdmin().getUsername().equals(currentUser.getUsername());
        if(currentUser.getRole().equals(Role.DEFAULT_USER)){
            Optional<JobApplication> application = jobOffer.getApplications()
                    .stream()
                    .filter(a -> a.getApplicant().getUsername().equals(currentUser.getUsername()))
                    .findFirst();
            model.addAttribute("hasApplied", application.isPresent());
            application.ifPresent(jobApplication -> model.addAttribute("jobApplication", jobApplication));
        }


        model.addAttribute("jobOffer", jobOffer);
        model.addAttribute("isOwnCompany", isOwnCompany);
        model.addAttribute("company", company);
        return "joboffer";
    }
}
