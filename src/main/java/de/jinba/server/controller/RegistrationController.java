package de.jinba.server.controller;

import de.jinba.server.dto.CompanyRegistrationRequest;
import de.jinba.server.dto.UserRegistrationRequest;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This controller handles the registration of users and companies.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService<UserRegistrationRequest> jinbaUserRegistrationService;
    private final RegistrationService<CompanyRegistrationRequest> jinbaCompanyRegistrationService;
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;

    /**
     * Shows the registration page for users.
     *
     * @param model The model.
     * @return The name of the registration template.
     */
    @GetMapping("/register")
    public String viewRegisterUserPage(Model model) {
        if(appUserDetailsService.isAuthenticated()){
            return "redirect:/dashboard";
        }
        model.addAttribute("registerForm", new UserRegistrationRequest("",
                "", "", "", ""));
        model.addAttribute("error", null);
        return "register";
    }

    /**
     * Registers a user.
     *
     * @param request The registration request.
     * @param result  The binding result. Used for input validation.
     * @return Redirects to the login page, when the registration was successful. Otherwise, the registration page is shown again.
     */
    @PostMapping("/register")
    public String doRegisterUser(@Valid @ModelAttribute("registerForm") UserRegistrationRequest request,
                                 BindingResult result) {
        if(appUserDetailsService.isAuthenticated()){
            return "redirect:/dashboard";
        }
        if (appUserDetailsService.existsByUsername(request.getEmail())) {
            result.rejectValue("email", "error.registerForm", "Email already exists");
        }
        if (result.hasErrors()) {
            return "register";
        }
        jinbaUserRegistrationService.register(request);
        return "redirect:/login";
    }

    /**
     * Shows the registration page for companies.
     *
     * @param model The model.
     * @return The name of the registration template.
     */
    @GetMapping("/register/business")
    public String viewRegisterCompanyPage(Model model) {
        if(appUserDetailsService.isAuthenticated()){
            return "redirect:/dashboard";
        }
        model.addAttribute("registerForm", new CompanyRegistrationRequest());
        return "register-company";
    }

    /**
     * Registers a company.
     *
     * @param request The registration request.
     * @param result  The binding result. Used for input validation.
     * @return Redirects to the login page when input is valid. Otherwise, returns the registration page.
     */
    @PostMapping("/register/business")
    public String doRegisterCompany(@Valid @ModelAttribute("registerForm") CompanyRegistrationRequest request,
                                    BindingResult result) {
        if(appUserDetailsService.isAuthenticated()){
            return "redirect:/dashboard";
        }
        if (appUserDetailsService.existsByUsername(request.getUser().getEmail())) {
            result.rejectValue("user.email", "error.registerForm", "Email already exists");
        }
        if (companyDetailsService.existsByName(request.getCompany().getName())) {
            result.rejectValue("company.name", "error.registerForm", "Company name already exists");
        }
        if (result.hasErrors()) {
            return "register-company";
        }
        jinbaCompanyRegistrationService.register(request);
        return "redirect:/login";
    }
}
