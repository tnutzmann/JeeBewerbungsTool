package de.jinba.server.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller handles the home page.
 */
@Controller
public class HomeController {
    /**
     * Shows the home page.
     *
     * @return the template.
     */
    @GetMapping(value = {"/", "/home"})
    public String viewHomePage() {
        return "home";
    }
}
