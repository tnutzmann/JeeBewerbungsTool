package de.jinba.server.service;

import de.jinba.server.dto.CompanyRegistrationRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.enumuration.Role;
import de.jinba.server.exception.ParameterInvalidException;
import de.jinba.server.repository.AppUserRepository;
import de.jinba.server.repository.CompanyRepository;
import de.jinba.server.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JinbaCompanyRegistrationService implements RegistrationService<CompanyRegistrationRequest> {
    private final AppUserDetailsService appUserDetailsService;
    private final CompanyRepository companyRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(CompanyRegistrationRequest request) {
        if (!ValidationUtil.isValidPassword(request.getUser().getPassword())) {
            throw new ParameterInvalidException("Password is invalid");
        }
        if (!ValidationUtil.isValidEmail(request.getUser().getEmail())) {
            throw new ParameterInvalidException("Email is invalid");
        }
        AppUser draftUser = AppUser.builder()
                .role(Role.COMPANY_USER)
                .details(request.getUser().getDetails())
                .email(request.getUser().getEmail())
                .firstName(request.getUser().getFirstName())
                .lastName(request.getUser().getLastName())
                .password(passwordEncoder.encode(request.getUser().getPassword()))
                .locked(false)
                .enabled(true)
                .skills(new ArrayList<>())
                .build();
        AppUser savedUser = appUserRepository.save(draftUser);
        Company draftCompany = Company.builder()
                .name(request.getCompany().getName())
                .description(request.getCompany().getDescription())
                .admin(savedUser)
                .build();
        Company savedCompany = companyRepository.save(draftCompany);
    }
}
