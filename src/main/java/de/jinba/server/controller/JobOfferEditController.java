package de.jinba.server.controller;


import de.jinba.server.dto.JobOfferEditRequest;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.service.AppUserDetailsService;
import de.jinba.server.service.CompanyDetailsService;
import de.jinba.server.service.JobOfferService;
import de.jinba.server.util.ModelConfigurer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JobOfferEditController {
    public static final String JOB_OFFER_EDIT_FORM = "jobOfferEditForm";
    private final JobOfferService jobOfferService;
    private final CompanyDetailsService companyDetailsService;

    @GetMapping("offer/edit/{id}")
    public String viewJobOfferEditPage(@PathVariable String id, Model model) {
        JobOffer jobOffer = jobOfferService.getById(id);
        Company company = companyDetailsService.findCompanyOfCurrentUser();

        // check if the Company owns the JobOffer
        if(company.getJobOffers().contains(jobOffer)) {
            ModelConfigurer.of(model).with(JOB_OFFER_EDIT_FORM, new JobOfferEditRequest(jobOffer));
            return "joboffer-edit";
        }
        throw new IllegalAccessException("You are not Authorized to edit this JobOffer!");
    }

}
