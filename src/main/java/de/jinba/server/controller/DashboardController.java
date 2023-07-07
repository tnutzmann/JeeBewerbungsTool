package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * This controller handles the dashboard page.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;
    private final JobOfferService jobOfferService;

    /**
     * Shows the dashboard page.
     *
     * @param model the model.
     * @return the template.
     */
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        AppUser profile = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated user attempted to access the dashboard."));
        model.addAttribute("profile", profile);
        if(profile.getRole().equals(Role.COMPANY_USER)) {
                    model.addAttribute("applicationsList", companyDetailsService.findByAdminId(profile.getId())
                            .orElseThrow(() -> new EntityNotFoundException("No Company for current User could be found!"))
                            .getJobOffers().stream()
                            .map(JobOffer::getApplications)
                            .flatMap(List::stream)
                            .toList());
        }
        if(profile.getRole().equals(Role.DEFAULT_USER)){
            model.addAttribute("jobOffers", jobOfferService.findJobOfferSuggestionsByUser(profile));
            model.addAttribute("hasSearched", false);
        }
        return "dashboard";
    }

}
