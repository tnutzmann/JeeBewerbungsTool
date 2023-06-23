package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;
    private final JobOfferDetailsService jobOfferDetailsService;

    @GetMapping("/profile")
    public String viewOwnProfile(){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        if (currentUser.getRole() == Role.DEFAULT_USER){
            return String.format("redirect:/profile/%s", currentUser.getId());
        }
        String companyId = companyDetailsService.findByAdminId(currentUser.getId())
                .orElseThrow(EntityNotFoundException::new).getId();
        return String.format("redirect:/company/%s", companyId);
    }

    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable String id,
                              Model model){
        AppUser profile = appUserDetailsService.getById(id);
        if (profile.getRole() == Role.COMPANY_USER){
            String companyId = companyDetailsService.findByAdminId(profile.getId())
                    .orElseThrow(EntityNotFoundException::new).getId();
            return String.format("redirect:/company/%s", companyId);
        }
        boolean isOwnProfile = profile.getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        profile.getSkills().sort((o1, o2) -> o2.getLevel().ordinal() - o1.getLevel().ordinal());
        model.addAttribute("profile", profile);
        model.addAttribute("isOwnProfile", isOwnProfile);
        return "profile";
    }

    @GetMapping("/company/{id}")
    public String viewCompany(@PathVariable String id,
                              Model model){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        Company company = companyDetailsService.getById(id);
        boolean isOwnCompany = company.getAdmin().getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        model.addAttribute("company", company);
        model.addAttribute("isOwnProfile", isOwnCompany);
        model.addAttribute("unappliedJobIds", jobOfferDetailsService.getAllUnappliedJobOffersByCompany(currentUser, company)
                .stream()
                .map(JobOffer::getId).toList());
        return "company";
    }
}
