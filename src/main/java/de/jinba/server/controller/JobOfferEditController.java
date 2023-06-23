package de.jinba.server.controller;


import de.jinba.server.dto.JobOfferEditRequest;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferService;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class JobOfferEditController {
    public static final String JOB_OFFER_EDIT_FORM = "jobOfferEditForm";
    public static final String BINDING_RESULT_PREFIX = "org.springframework.validation.BindingResult.%s";
    private final JobOfferService jobOfferService;
    private final CompanyDetailsService companyDetailsService;

    @GetMapping("offer/{id}/edit")
    public String viewJobOfferEditPage(@PathVariable String id, Model model) {
        JobOffer jobOffer = jobOfferService.getById(id);
        Company company = companyDetailsService.findCompanyOfCurrentUser();

        // check if the Company owns the JobOffer
        if(company.getId().equals(jobOffer.getCompany().getId())) {
            ModelConfigurer.of(model)
                    .with(JOB_OFFER_EDIT_FORM, new JobOfferEditRequest(jobOffer));
            return "joboffer-edit";
        }
        throw new UnauthorizedException("You are not Authorized to edit this JobOffer!");
    }

    @PostMapping("offer/{id}/edit")
    public String updateJobOffer(@PathVariable String id, @ModelAttribute(JOB_OFFER_EDIT_FORM) @Valid JobOfferEditRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(JOB_OFFER_EDIT_FORM, request)
                    .with(String.format(BINDING_RESULT_PREFIX, JOB_OFFER_EDIT_FORM), bindingResult);
            return String.format("redirect:/offer/%s/edit", id);
        }
        JobOffer oldJobOffer = jobOfferService.getById(id);
        JobOffer updatedJobOffer = jobOfferService.updateJobOffer(oldJobOffer, request);
        return String.format("redirect:/offer/%s", id);
    }
}
