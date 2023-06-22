package de.jinba.server.controller;

import de.jinba.server.dto.*;
import de.jinba.server.entity.enumuration.SkillLevel;
import de.jinba.server.service.SkillService;
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
public class ProfileManagementController {
    public static final String SKILLS = "skills";
    public static final String SKILL_ADD_FORM = "addSkillForm";
    public static final String PASSWORD_FORM = "changePasswordForm";
    public static final String EMAIL_FORM = "changeEmailForm";
    public static final String ACCOUNT_INFORMATION_FORM = "changeAccountInformationForm";
    public static final String BINDING_RESULT_PREFIX = "org.springframework.validation.BindingResult.%s";
    public static final String COMPANY_DESCRIPTION_FORM = "changeCompanyDescriptionForm";
    public static final String COMPANY_DETAILS_FORM = "changeCompanyDetailsForm";

    private final AppUserDetailsService appUserDetailsService;
    private final CompanyDetailsService companyDetailsService;
    private final SkillService skillService;

    @GetMapping("/profile/edit")
    public String viewEditProfilePage(Model model) {
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
            configurer.with(SKILLS, currentUser.getSkills()
                            .stream()
                            .map(AppUserSkillDto::from)
                            .toList())
                    .with(SKILL_ADD_FORM, new SkillAddRequest("", SkillLevel.BEGINNER));
        }else {
            Company company = companyDetailsService.findByAdminId(currentUser.getId())
                    .orElseThrow(() -> new IllegalStateException("User does not administrate a company"));
            configurer.with(COMPANY_DETAILS_FORM, new CompanyDetailsChangeRequest(company.getName()))
                    .with(COMPANY_DESCRIPTION_FORM, new CompanyDescriptionChangeRequest(company.getDescription()));
        }
        return "profile-edit";
    }

    @PostMapping("/profile/edit/user/password")
    public String changePassword(@ModelAttribute(PASSWORD_FORM) @Valid PasswordChangeRequest passwordChangeRequest,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser().orElseThrow();
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.changePasswordForm", "Passwords do not match");
        }
        if(!appUserDetailsService.isCorrectPassword(currentUser, passwordChangeRequest.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "error.changePasswordForm", "Current password is incorrect");
        }
        if (bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, PASSWORD_FORM), bindingResult)
                    .with(PASSWORD_FORM, passwordChangeRequest);
            return "redirect:/profile/edit";
        }
        appUserDetailsService.changePassword(currentUser, passwordChangeRequest);
        return "redirect:/profile/edit?success=Changed Password!";
    }

    @PostMapping("/profile/edit/user/email")
    public String changeEmail(@ModelAttribute(EMAIL_FORM) @Valid EmailChangeRequest emailChangeRequest,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (appUserDetailsService.existsByUsername(emailChangeRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.changeEmailForm", "Email already exists");
        }
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, EMAIL_FORM), bindingResult)
                    .with(EMAIL_FORM, emailChangeRequest);
            return "redirect:/profile/edit";
        }
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow();
        appUserDetailsService.changeEmail(currentUser, emailChangeRequest);
        return "redirect:/profile/edit?success=Changed Email!";
    }



    @PostMapping("/profile/edit/user/account-information")
    public String changeAccountInformation(@ModelAttribute(ACCOUNT_INFORMATION_FORM) @Valid AccountInformationChangeRequest accountInformationChangeRequest,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, ACCOUNT_INFORMATION_FORM), bindingResult)
                    .with(ACCOUNT_INFORMATION_FORM, accountInformationChangeRequest);
            return "redirect:/profile/edit";
        }
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                        .orElseThrow();
        appUserDetailsService.changeAccountInformation(currentUser, accountInformationChangeRequest);
        return "redirect:/profile/edit?success=Changed Account Information!";
    }

    @PostMapping("/profile/edit/company/description")
    public String changeCompanyDescription(@ModelAttribute(COMPANY_DESCRIPTION_FORM) @Valid CompanyDescriptionChangeRequest companyDescriptionChangeRequest,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, COMPANY_DESCRIPTION_FORM), bindingResult)
                    .with(COMPANY_DESCRIPTION_FORM, companyDescriptionChangeRequest);
            return "redirect:/profile/edit";
        }
        Company company = companyDetailsService.findCompanyOfCurrentUser();
        companyDetailsService.changeDescription(company, companyDescriptionChangeRequest);
        return "redirect:/profile/edit?success=Changed Company Description!";
    }

    @PostMapping("/profile/edit/company/details")
    public String changeCompanyDetails(@ModelAttribute(COMPANY_DETAILS_FORM) @Valid CompanyDetailsChangeRequest companyDetailsChangeRequest,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, COMPANY_DETAILS_FORM), bindingResult)
                    .with(COMPANY_DETAILS_FORM, companyDetailsChangeRequest);
            return "redirect:/profile/edit";
        }
        Company company = companyDetailsService.findCompanyOfCurrentUser();
        companyDetailsService.changeCompanyDetails(company, companyDetailsChangeRequest);
        return "redirect:/profile/edit?success=Changed Company Details!";
    }

    @PostMapping("/profile/edit/skills/add")
    public String addSkill(@ModelAttribute("addSkillForm") @Valid SkillAddRequest skillAddRequest,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if(skillService.hasCurrentUserSkill(skillAddRequest.getSkillName())) {
            bindingResult.rejectValue("skillName", "error.addSkillForm", "Already has skill");
        }
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, SKILL_ADD_FORM), bindingResult)
                    .with(SKILL_ADD_FORM, skillAddRequest);
            return "redirect:/profile/edit";
        }
        skillService.addSkillToUser(skillAddRequest);
        return "redirect:/profile/edit?success=Added Skill!";
    }

    @GetMapping("/profile/edit/skills/remove/{id}")
    public String removeSkill(@PathVariable("id") Long id) {
        skillService.removeSkillFromUser(id);
        return "redirect:/profile/edit?success=Removed Skill!";
    }
}
