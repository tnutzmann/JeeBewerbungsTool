package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final AppUserDetailsService appUserDetailsService;
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        AppUser profile = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to access profile page"));
        boolean isOwnProfile = profile.getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        profile.getSkills().sort((o1, o2) -> o2.getLevel().ordinal() - o1.getLevel().ordinal());
        model.addAttribute("profile", profile);
        model.addAttribute("isOwnProfile", isOwnProfile);
        return "dashboard";
    }

}
