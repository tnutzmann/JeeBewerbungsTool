package de.jinba.server.controller;


import de.jinba.server.dto.JobOfferEditRequest;
import de.jinba.server.dto.SkillAddRequest;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferService;
import de.jinba.server.service.SkillService;
import de.jinba.server.util.ModelConfigurer;
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

/**
 * This controller handles the editing of job offers.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class JobOfferEditController {
    public static final String JOB_OFFER_EDIT_FORM = "jobOfferEditForm";
    public static final String SKILL_ADD_FORM = "addSkillForm";
    public static final String BINDING_RESULT_PREFIX = "org.springframework.validation.BindingResult.%s";
    private final JobOfferService jobOfferService;
    private final CompanyDetailsService companyDetailsService;
    private final SkillService skillService;

    /**
     * Shows the job offer edit page.
     *
     * @param id    the id of the job offer.
     * @param model the model.
     * @return the template.
     */
    @GetMapping("/offer/{id}/edit")
    public String viewJobOfferEditPage(@PathVariable String id, Model model) {
        JobOffer jobOffer = jobOfferService.getById(id);
        Company company = companyDetailsService.findCompanyOfCurrentUser();

        // check if the Company owns the JobOffer
        if (!company.getId().equals(jobOffer.getCompany().getId())) {
            throw new UnauthorizedException("You are not Authorized to edit this JobOffer!");
        }
        ModelConfigurer.of(model)
                .with(JOB_OFFER_EDIT_FORM, new JobOfferEditRequest(jobOffer))
                .with(SKILL_ADD_FORM, new SkillAddRequest())
                .with("skills", jobOffer.getSkills());
        return "joboffer-edit";
    }

    /**
     * Updates the job offer.
     *
     * @param id                 the id of the job offer.
     * @param request            the request.
     * @param bindingResult      the binding result.
     * @param redirectAttributes the redirect attributes.
     * @return redirect to the job offer page.
     */
    @PostMapping("/offer/{id}/edit")
    public String updateJobOffer(@PathVariable String id, @ModelAttribute(JOB_OFFER_EDIT_FORM) @Valid JobOfferEditRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(JOB_OFFER_EDIT_FORM, request)
                    .with(String.format(BINDING_RESULT_PREFIX, JOB_OFFER_EDIT_FORM), bindingResult);
            return String.format("redirect:/offer/%s/edit", id);
        }
        JobOffer oldJobOffer = jobOfferService.getById(id);
        JobOffer updatedJobOffer = jobOfferService.updateJobOffer(oldJobOffer, request);
        return String.format("redirect:/offer/%s", id);
    }

    /**
     * Adds a skill to the job offer.
     *
     * @param skillAddRequest    the skill add request.
     * @param bindingResult      the binding result.
     * @param redirectAttributes the redirect attributes.
     * @param id                 the id of the job offer.
     * @return redirect to the job offer edit page.
     */
    @PostMapping("/offer/{id}/edit/skills/add")
    public String addSkill(@ModelAttribute("addSkillForm") @Valid SkillAddRequest skillAddRequest,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @PathVariable String id) {
        JobOffer jobOffer = jobOfferService.getById(id);
        Company company = companyDetailsService.findCompanyOfCurrentUser();

        if (!company.getId().equals(jobOffer.getCompany().getId())) {
            throw new UnauthorizedException("You are not Authorized to edit this JobOffer!");
        }
        if (skillService.hasJobOfferSkill(skillAddRequest.getSkillName(), jobOffer)) {
            bindingResult.rejectValue("skillName", "error.addSkillForm", "Already has skill");
        }
        if (bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(String.format(BINDING_RESULT_PREFIX, SKILL_ADD_FORM), bindingResult)
                    .with(SKILL_ADD_FORM, skillAddRequest);
            return String.format("redirect:/offer/%s/edit", id);
        }
        skillService.addSkillToJobOffer(jobOffer, skillAddRequest);
        return String.format("redirect:/offer/%s/edit?success=Added Skill!", id);
    }

    /**
     * Removes a skill from the job offer.
     *
     * @param skillId    the id of the skill.
     * @param jobOfferId the id of the job offer.
     * @return redirect to the job offer edit page.
     */
    @GetMapping("/offer/{jobOfferId}/edit/skills/remove/{skillId}")
    public String removeSkill(@PathVariable("skillId") Long skillId, @PathVariable("jobOfferId") String jobOfferId) {
        JobOffer jobOffer = jobOfferService.getById(jobOfferId);
        Company company = companyDetailsService.findCompanyOfCurrentUser();

        if (!company.getId().equals(jobOffer.getCompany().getId())) {
            throw new UnauthorizedException("You are not Authorized to edit this JobOffer!");
        }

        skillService.removeSkillFromJobOffer(jobOffer, skillId);
        return String.format("redirect:/offer/%s/edit?success=Removed Skill!", jobOfferId);
    }

}
