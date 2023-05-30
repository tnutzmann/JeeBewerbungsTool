package de.jinba.server.service;

import de.jinba.server.dto.CompanyDescriptionChangeRequest;
import de.jinba.server.dto.CompanyDetailsChangeRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyDetailsService {
    private final CompanyRepository companyRepository;
    private final AppUserDetailsService appUserDetailsService;

    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
    public Optional<Company> findByAdminId(String id) {
        return companyRepository.findByAdmin_Id(id);
    }

    public void changeCompanyDetails(CompanyDetailsChangeRequest request) {
        if(existsByName(request.getName()))
            throw new IllegalStateException(String.format("Company with name %s already exists", request.getName()));
        Company company = findCompanyOfCurrentUser();
        company.setName(request.getName());
        companyRepository.save(company);
    }

    public void changeDescription(CompanyDescriptionChangeRequest request) {
        Company company = findCompanyOfCurrentUser();
        company.setDescription(request.getDescription());
        companyRepository.save(company);
    }

    public Company findCompanyOfCurrentUser(){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to access profile page"));
        if(currentUser.getRole() != Role.COMPANY_USER)
            throw new IllegalStateException("User does not administrate a company");
        return findByAdminId(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("User does not administrate a company, even though he is a COMPANY_USER"));
    }


}
