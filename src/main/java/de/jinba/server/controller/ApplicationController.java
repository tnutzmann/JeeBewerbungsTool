package de.jinba.server.controller;

import de.jinba.server.entity.AppUser;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    private final AppUserDetailsService appUserDetailsService;
    private final ApplicationService applicationService;
    @GetMapping("/offer/{id}/apply")
    public String applyForJobOffer(@PathVariable String id){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        applicationService.applyForJobOffer(currentUser, id);
        return String.format("redirect:/offer/%s?success=You have successfully applied to the job.", id);
    }

    @GetMapping("/application/{id}/accept")
    public String acceptApplication(@PathVariable String id, @RequestParam String message){
        applicationService.acceptApplication(id, message);
        return "redirect:/profile";
    }

    @GetMapping("/application/{id}/reject")
    public String rejectApplication(@PathVariable String id,
                                    @RequestParam String message){
        applicationService.rejectApplication(id, message);
        return "redirect:/profile";
    }

}
