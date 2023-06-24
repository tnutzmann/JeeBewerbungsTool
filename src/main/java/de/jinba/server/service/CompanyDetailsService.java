package de.jinba.server.service;

import de.jinba.server.dto.CompanyDescriptionChangeRequest;
import de.jinba.server.dto.CompanyDetailsChangeRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.EntityAlreadyExistsException;
import de.jinba.server.exception.EntityNotFoundException;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for handling company details.
 */
@Service
@RequiredArgsConstructor
public class CompanyDetailsService {
    private final CompanyRepository companyRepository;
    private final AppUserDetailsService appUserDetailsService;

    /**
     * Checks if company exists by name.
     *
     * @param name Name of company.
     * @return True if company exists, false otherwise.
     */
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }

    /**
     * Finds company by admin id.
     *
     * @param id Id of admin.
     * @return Company.
     */
    public Optional<Company> findByAdminId(String id) {
        return companyRepository.findByAdmin_Id(id);
    }

    /**
     * Changes company details.
     *
     * @param company the company to be changed.
     * @param request the request containing the new details.
     */
    public void changeCompanyDetails(Company company, CompanyDetailsChangeRequest request) {
        if (existsByName(request.getName()))
            throw new EntityAlreadyExistsException(String.format("Company with name %s already exists", request.getName()));
        company.setName(request.getName());
        companyRepository.save(company);
    }

    /**
     * Changes company description.
     *
     * @param company the company to be changed.
     * @param request the request containing the new description.
     */
    public void changeDescription(Company company,
                                  CompanyDescriptionChangeRequest request) {
        company.setDescription(request.getDescription());
        companyRepository.save(company);
    }

    /**
     * Finds company of current user.
     *
     * @return Company.
     */
    public Company findCompanyOfCurrentUser() {
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated user attempted to access profile page"));
        if (currentUser.getRole() != Role.COMPANY_USER)
            throw new UnauthorizedException("User does not administrate a company");
        return findByAdminId(currentUser.getId())
                .orElseThrow(() -> new UnauthorizedException("User does not administrate a company, even though he is a COMPANY_USER"));
    }

    public Company getById(String id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Could not find company with id: %s", id)));
    }


}
