package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This controller handles job application related pages and endpoints.
 */
@Controller
@RequiredArgsConstructor
public class ApplicationController {
    private final AppUserDetailsService appUserDetailsService;
    private final ApplicationService applicationService;

    /**
     * Method to apply for a job offer. The user must be logged in.
     *
     * @param id the id of the job offer.
     * @return the redirect prompt.
     */
    @GetMapping("/offer/{id}/apply")
    public String applyForJobOffer(@PathVariable String id) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized user attempted to apply for job offer"));
        if (currentUser.getRole() == Role.COMPANY_USER) {
            throw new UnauthorizedException("Company user attempted to apply for job offer");
        }
        applicationService.applyForJobOffer(currentUser, id);
        return String.format("redirect:/offer/%s?success=You have successfully applied to the job.", id);
    }

    @GetMapping("/application/{id}/accept")
    public String acceptApplication(@PathVariable String id, @RequestParam String message) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized user attempted to accept application"));
        applicationService.acceptApplication(id, message, currentUser);
        return "redirect:/dashboard";
    }

    @GetMapping("/application/{id}/reject")
    public String rejectApplication(@PathVariable String id,
                                    @RequestParam String message) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized user attempted to reject application"));
        applicationService.rejectApplication(id, message, currentUser);
        return "redirect:/dashboard";
    }

}
