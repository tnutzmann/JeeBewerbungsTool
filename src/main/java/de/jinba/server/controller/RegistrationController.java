package de.jinba.server.controller;

import de.jinba.server.dto.RegisterRequest;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.JinbaUserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final JinbaUserRegistrationService jinbaUserRegistrationService;
    private final AppUserDetailsService appUserDetailsService;

    @GetMapping("/register")
    public String viewRegisterUserPage(Model model) {
        model.addAttribute("registerForm", new RegisterRequest("",
                "", "", "", ""));
        model.addAttribute("error", null);
        return "register";
    }

    @PostMapping("/register")
    public String doRegisterUser(@Valid @ModelAttribute("registerForm") RegisterRequest request,
                                 BindingResult result) {
        if (appUserDetailsService.existsByUsername(request.getEmail())) {
            result.rejectValue("email", "error.registerForm", "Email already exists");
        }
        if (result.hasErrors()) {
            return "register";
        }
        jinbaUserRegistrationService.register(request);
        return "redirect:/login";
    }
}
