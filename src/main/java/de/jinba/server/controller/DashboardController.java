package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.util.ModelConfigurer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

/**
 * This controller handles the dashboard page.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;

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
            ModelConfigurer.of(model)
                    .with("applications", companyDetailsService.findCompanyOfCurrentUser().getJobOffers().stream()
                            .map(JobOffer::getApplications).collect(Collectors.toList()));
            return "dashboard";
        }

        return "dashboard";
    }

}
