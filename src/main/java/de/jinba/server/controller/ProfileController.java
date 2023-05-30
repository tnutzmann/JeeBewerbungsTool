package de.jinba.server.controller;

import de.jinba.server.dto.*;
import de.jinba.server.util.ModelConfigurer;
import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    public static final String SKILLS = "skills";
    public static final String PASSWORD_FORM = "changePasswordForm";
    public static final String EMAIL_FORM = "changeEmailForm";
    public static final String ACCOUNT_INFORMATION_FORM = "changeAccountInformationForm";
    public static final String BINDING_RESULT_PREFIX = "org.springframework.validation.BindingResult.%s";
    public static final String COMPANY_DESCRIPTION_FORM = "changeCompanyDescriptionForm";
    public static final String COMPANY_DETAILS_FORM = "changeCompanyDetailsForm";
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;
    @GetMapping("/profile")
    public String viewProfilePage(Model model) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to access profile page"));
        ModelConfigurer configurer = ModelConfigurer.of(model)
                .with(PASSWORD_FORM, new PasswordChangeRequest())
                .with(EMAIL_FORM, new EmailChangeRequest(currentUser.getEmail()))
                .with(ACCOUNT_INFORMATION_FORM, new AccountInformationChangeRequest(
                        currentUser.getFirstName(),
                        currentUser.getLastName(),
                        currentUser.getDetails()));
        if(currentUser.getRole() == Role.DEFAULT_USER){
            configurer.with(SKILLS, currentUser.getSkills());
        }else {
            Company company = companyDetailsService.findByAdminId(currentUser.getId())
                    .orElseThrow(() -> new IllegalStateException("User does not administrate a company"));
            configurer.with(COMPANY_DETAILS_FORM, new CompanyDetailsChangeRequest(company.getName()))
                    .with(COMPANY_DESCRIPTION_FORM, new CompanyDescriptionChangeRequest(company.getDescription()));
        }
        return "profile";
    }

    @PostMapping("/profile/change/user/password")
    public String changePassword(@ModelAttribute("changePasswordForm") @Valid PasswordChangeRequest passwordChangeRequest,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.changePasswordForm", "Passwords do not match");
        }
        if(!appUserDetailsService.isCorrectPassword(passwordChangeRequest.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "error.changePasswordForm", "Current password is incorrect");
        }
        if (bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, PASSWORD_FORM), bindingResult)
                    .with(PASSWORD_FORM, passwordChangeRequest);
            return "redirect:/profile";
        }
        appUserDetailsService.changePassword(passwordChangeRequest);
        return "redirect:/profile?success=Changed Password!";
    }

    @PostMapping("/profile/change/user/email")
    public String changeEmail(@ModelAttribute("changeEmailForm") @Valid EmailChangeRequest emailChangeRequest,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (appUserDetailsService.existsByUsername(emailChangeRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.changeEmailForm", "Email already exists");
        }
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, EMAIL_FORM), bindingResult)
                    .with(EMAIL_FORM, emailChangeRequest);
            return "redirect:/profile";
        }
        appUserDetailsService.changeEmail(emailChangeRequest);
        return "redirect:/profile?success=Changed Email!";
    }



    @PostMapping("/profile/change/user/account-information")
    public String changeAccountInformation(@ModelAttribute("changeAccountInformationForm") @Valid AccountInformationChangeRequest accountInformationChangeRequest,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, ACCOUNT_INFORMATION_FORM), bindingResult)
                    .with(ACCOUNT_INFORMATION_FORM, accountInformationChangeRequest);
            return "redirect:/profile";
        }
        appUserDetailsService.changeAccountInformation(accountInformationChangeRequest);
        return "redirect:/profile?success=Changed Account Information!";
    }

    @PostMapping("/profile/change/company/description")
    public String changeCompanyDescription(@ModelAttribute("changeCompanyDescriptionForm") @Valid CompanyDescriptionChangeRequest companyDescriptionChangeRequest,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, COMPANY_DESCRIPTION_FORM), bindingResult)
                    .with(COMPANY_DESCRIPTION_FORM, companyDescriptionChangeRequest);
            return "redirect:/profile";
        }
        companyDetailsService.changeDescription(companyDescriptionChangeRequest);
        return "redirect:/profile?success=Changed Company Description!";
    }

    @PostMapping("/profile/change/company/details")
    public String changeCompanyDetails(@ModelAttribute("changeCompanyDetailsForm") @Valid CompanyDetailsChangeRequest companyDetailsChangeRequest,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, COMPANY_DETAILS_FORM), bindingResult)
                    .with(COMPANY_DETAILS_FORM, companyDetailsChangeRequest);
            return "redirect:/profile";
        }
        companyDetailsService.changeCompanyDetails(companyDetailsChangeRequest);
        return "redirect:/profile?success=Changed Company Details!";
    }

    @GetMapping("/profile/change/skills/remove/{id}")
    public String removeSkill(@PathVariable("id") Long id,
                              Model model) {
        appUserDetailsService.removeSkill(id);
        return String.format("redirect:/%s?success=Removed Skill!", viewProfilePage(model));
    }
}
