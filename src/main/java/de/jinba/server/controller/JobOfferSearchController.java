package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobOfferSearchController {
    private final JobOfferService jobOfferService;
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;
    @GetMapping("/search")
    public String searchForJobOffers(Model model,
                                     @RequestParam(name = "q", required = false) String query){
        AppUser profile = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated user attempted to access the dashboard."));
        if(profile.getRole().equals(Role.COMPANY_USER)) {
            throw new UnauthorizedException("Company user attempted to access the job offer search.");
        }
        model.addAttribute("profile", profile);
        model.addAttribute("jobOffers", jobOfferService.findJobOfferByQueryString(profile, query));
        model.addAttribute("hasSearched", true);
        return "dashboard";
    }
}
