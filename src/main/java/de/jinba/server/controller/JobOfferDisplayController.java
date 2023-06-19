package de.jinba.server.controller;

import de.jinba.server.entity.Company;
import de.jinba.server.service.CompanyDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class JobOfferDisplayController {
    private final CompanyDetailsService companyDetailsService;
    @GetMapping("/company/{id}/offer")
    public String viewCompanyOffersPage(Model model, @PathVariable String id){
        Company company = companyDetailsService.getById(id);
        boolean isOwnCompany = company.getAdmin().getUsername().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        model.addAttribute("company", company);
        model.addAttribute("isOwnCompany", isOwnCompany);
        return "company-offers";
    }
}
