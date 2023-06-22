package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    private final AppUserDetailsService appUserDetailsService;
    private final ApplicationService applicationService;
    @GetMapping("/offer/{id}/apply")
    public String applyForJobOffer(@PathVariable String id){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        applicationService.applyForJobOffer(currentUser, id);
        return String.format("redirect:/offer/%s", id);
    }
}
