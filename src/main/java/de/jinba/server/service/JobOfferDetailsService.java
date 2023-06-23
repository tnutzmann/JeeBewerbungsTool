package de.jinba.server.service;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.Company;
import de.jinba.server.entity.JobOffer;
import de.jinba.server.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOfferDetailsService {
    private final JobOfferRepository jobOfferRepository;

    public JobOffer getById(String id){
        return jobOfferRepository.findById(id).orElseThrow();
    }

    public List<JobOffer> getAll(){
        return jobOfferRepository.findAll();
    }

    public List<JobOffer> getAllUnappliedJobOffersByCompany(AppUser user, String companyId){
        return jobOfferRepository.findAllUnappliedJobOffersByCompanyAndUser(user.getId(), companyId);
    }
    public List<JobOffer> getAllUnappliedJobOffersByCompany(AppUser user, Company companyId){
        return this.getAllUnappliedJobOffersByCompany(user, companyId.getId());
    }

    public boolean hasUserAppliedToJobOffer(AppUser user, JobOffer jobOffer){
        return jobOffer.getApplications()
                .stream()
                .anyMatch(application -> application.getApplicant()
                        .getId()
                        .equals(user.getId()));
    }
}
