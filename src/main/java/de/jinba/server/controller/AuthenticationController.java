package de.jinba.server.controller;

import de.jinba.server.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller handles the custom login page. It does not handle the login itself.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AppUserDetailsService appUserDetailsService;
    /**
     * Shows the login page.
     *
     * @return the template.
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        if(appUserDetailsService.isAuthenticated()){
            return "redirect:/dashboard";
        }
        return "login";
    }
}
