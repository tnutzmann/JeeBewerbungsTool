package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to access profile page"));
        return "dashboard";
    }

}
