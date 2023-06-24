package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller handles the dashboard page.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final AppUserDetailsService appUserDetailsService;

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
        return "dashboard";
    }

}
