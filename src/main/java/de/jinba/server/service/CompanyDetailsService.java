package de.jinba.server.service;

import de.jinba.server.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyDetailsService {
    private final CompanyRepository companyRepository;

    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
}
