package de.jinba.server.controller;

import de.jinba.server.dto.JobOfferCreationRequest;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

/**
 * This controller handles the creation of job offers.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class JobOfferCreationController {
    public static final String JOB_OFFER_CREATION_FORM = "jobOfferCreationForm";
    public static final String BINDING_RESULT_PREFIX = "org.springframework.validation.BindingResult.%s";
    private final JobOfferService jobOfferService;
    private final CompanyDetailsService companyDetailsService;

    /**
     * Shows the job offer creation page.
     *
     * @param model the model.
     * @return the template.
     */
    @GetMapping("/offer/create")
    public String viewJobOfferCreationPage(Model model) {
        ModelConfigurer.of(model)
                .with(JOB_OFFER_CREATION_FORM, new JobOfferCreationRequest("", "", "", new ArrayList<>()));
        return "joboffer-create";
    }

    /**
     * Creates a new job offer.
     *
     * @param request            the job offer creation request.
     * @param bindingResult      the binding result. Used for input validation.
     * @param redirectAttributes the redirect attributes.
     * @return redirects to the job offer page, when the creation was successful. Otherwise, the job offer creation page is shown again.
     */
    @PostMapping("/offer/create")
    public String createJobOffer(@ModelAttribute(JOB_OFFER_CREATION_FORM) @Valid JobOfferCreationRequest request,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelConfigurer.of(redirectAttributes)
                    .with(JOB_OFFER_CREATION_FORM, request)
                    .with(String.format(BINDING_RESULT_PREFIX, JOB_OFFER_CREATION_FORM), bindingResult);
            return "redirect:/offer/create";
        }
        if (request.getSkills() == null) {
            request.setSkills(new ArrayList<>());
        }
        Company company = companyDetailsService.findCompanyOfCurrentUser();
        JobOffer createdOffer = jobOfferService.addNewJobOffer(company, request);
        return String.format("redirect:/offer/%s", createdOffer.getId());
    }
}
