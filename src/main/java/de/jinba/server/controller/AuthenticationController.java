package de.jinba.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller handles the custom login page. It does not handle the login itself.
 */
@Controller
public class AuthenticationController {
    /**
     * Shows the login page.
     *
     * @return the template.
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }
}
