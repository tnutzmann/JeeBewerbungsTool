package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This controller handles the profile page.
 */
@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;
    private final JobOfferService jobOfferService;

    /**
     * Redirects to the profile page of the current user. If the current user is a company user, the company page is
     * shown instead.
     *
     * @return the redirect prompt.
     */
    @GetMapping("/profile")
    public String viewOwnProfile() {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        if (currentUser.getRole() == Role.DEFAULT_USER) {
            return String.format("redirect:/profile/%s", currentUser.getId());
        }
        String companyId = companyDetailsService.findByAdminId(currentUser.getId())
                .orElseThrow(EntityNotFoundException::new).getId();
        return String.format("redirect:/company/%s", companyId);
    }

    /**
     * Shows the profile page of the user with the given id.
     *
     * @param id    the id of the user.
     * @param model the model.
     * @return the template.
     */
    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable String id,
                              Model model) {
        AppUser profile = appUserDetailsService.getById(id);
        // If the user is a company user, redirect to the company page.
        if (profile.getRole() == Role.COMPANY_USER) {
            String companyId = companyDetailsService.findByAdminId(profile.getId())
                    .orElseThrow(EntityNotFoundException::new).getId();
            return String.format("redirect:/company/%s", companyId);
        }
        // Check if the profile is the own profile.
        boolean isOwnProfile = profile.getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        // Sort the skills by level.
        profile.getSkills().sort((o1, o2) -> o2.getLevel().ordinal() - o1.getLevel().ordinal());
        model.addAttribute("profile", profile);
        model.addAttribute("isOwnProfile", isOwnProfile);
        return "profile";
    }

    /**
     * Shows the company page of the company with the given id.
     *
     * @param id    the id of the company.
     * @param model the model.
     * @return the template.
     */
    @GetMapping("/company/{id}")
    public String viewCompany(@PathVariable String id,
                              Model model) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Company company = companyDetailsService.getById(id);
        // Check if the company is the own company.
        boolean isOwnCompany = company.getAdmin().getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        model.addAttribute("company", company);
        model.addAttribute("isOwnProfile", isOwnCompany);
        // Get all unapplied job offers of the company. This is used to show the "apply" button on the job offer cards.
        if (currentUser.getRole() == Role.DEFAULT_USER) {
            model.addAttribute("unappliedJobIds", jobOfferService.getAllUnappliedJobOffersByCompany(currentUser, company)
                    .stream()
                    .map(JobOffer::getId).toList());
        }
        return "company";
    }
}
