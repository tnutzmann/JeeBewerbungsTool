package de.jinba.server.service;

import de.jinba.server.dto.SkillAddRequest;
import de.jinba.server.entity.*;
import de.jinba.server.exception.EntityAlreadyExistsException;
import de.jinba.server.exception.UnauthorizedException;
import de.jinba.server.repository.AppUserRepository;
import de.jinba.server.repository.AppUserSkillRepository;
import de.jinba.server.repository.JobOfferSkillRepository;
import de.jinba.server.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for handling skills.
 */
@Service
@RequiredArgsConstructor
public class SkillService {
    private final AppUserDetailsService appUserDetailsService;
    private final AppUserRepository appUserRepository;
    private final SkillRepository skillRepository;
    private final AppUserSkillRepository appUserSkillRepository;
    private final JobOfferSkillRepository jobOfferSkillRepository;

    /**
     * Removes skill from user.
     *
     * @param user User to remove skill from.
     * @param id   Id of skill to remove.
     */
    public void removeSkillFromUser(AppUser user, Long id) {
        appUserSkillRepository.deleteById(id);
        appUserRepository.save(user);
    }

    /**
     * Adds skill to user.
     *
     * @param user    User to add skill to.
     * @param request Request containing skill name and level.
     */
    public void addSkillToUser(AppUser user, SkillAddRequest request) {
        Optional<Skill> existingSkill = skillRepository.findByName(request.getSkillName());
        if (existingSkill.isPresent()) {
            if (hasUserSkill(user, request.getSkillName())) {
                throw new EntityAlreadyExistsException("User already has skill");
            }
            AppUserSkill appUserSkill = AppUserSkill.builder()
                    .appUser(user)
                    .level(request.getLevel())
                    .skill(existingSkill.get())
                    .build();
            appUserSkillRepository.save(appUserSkill);
        } else {
            Skill createdSkill = createSkill(request.getSkillName());
            AppUserSkill appUserSkill = AppUserSkill.builder()
                    .appUser(user)
                    .level(request.getLevel())
                    .skill(createdSkill)
                    .build();
            appUserSkillRepository.save(appUserSkill);
        }
    }

    /**
     * Removes skill from job offer.
     *
     * @param jobOffer Job offer to remove skill from.
     * @param id       Id of skill to remove.
     */
    public void removeSkillFromJobOffer(JobOffer jobOffer, Long id) {
        jobOfferSkillRepository.deleteById(id);
    }

    /**
     * Adds skill to job offer.
     *
     * @param jobOffer Job offer to add skill to.
     * @param request  Request containing skill name and level.
     */
    public void addSkillToJobOffer(JobOffer jobOffer, SkillAddRequest request) {
        Optional<Skill> existingSkill = skillRepository.findByName(request.getSkillName());
        if (existingSkill.isPresent()) {
            if (hasJobOfferSkill(request.getSkillName(), jobOffer)) {
                throw new EntityAlreadyExistsException("JobOffer already has skill");
            }
            JobOfferSkill jobOfferSkill = JobOfferSkill.builder()
                    .jobOffer(jobOffer)
                    .level(request.getLevel())
                    .skill(existingSkill.get())
                    .build();
            jobOfferSkillRepository.save(jobOfferSkill);
        } else {
            Skill createdSkill = createSkill(request.getSkillName());
            JobOfferSkill jobOfferSkill = JobOfferSkill.builder()
                    .jobOffer(jobOffer)
                    .level(request.getLevel())
                    .skill(createdSkill)
                    .build();
            jobOfferSkillRepository.save(jobOfferSkill);
        }

    }

    /**
     * Creates skill.
     *
     * @param name Name of skill to create.
     * @return Created skill.
     */
    public Skill createSkill(String name) {
        Skill skill = Skill.builder()
                .name(name)
                .build();
        return skillRepository.save(skill);
    }

    /**
     * Gets skill by name if exists, otherwise creates it.
     *
     * @param name Name of skill to get.
     * @return Skill.
     */
    public Skill getSkillAndCreateIfNotExists(String name) {
        Optional<Skill> skill = skillRepository.findByName(name);
        return skill.orElseGet(() -> createSkill(name));
    }

    /**
     * Checks if the given user has the given skill.
     *
     * @param user User to check.
     * @param name Name of skill to check.
     * @return True if current user has skill, false otherwise.
     */
    public boolean hasUserSkill(AppUser user, String name) {
        return user.getSkills().stream().anyMatch(skill -> skill.getSkill().getName().equals(name));
    }

    /**
     * Checks if job offer has skill.
     *
     * @param name     Name of skill to check.
     * @param jobOffer Job offer to check.
     * @return True if job offer has skill, false otherwise.
     */
    public boolean hasJobOfferSkill(String name, JobOffer jobOffer) {
        appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated user attempted to add skill to JobOffer."));
        return jobOffer.getSkills().stream().anyMatch(skill -> skill.getSkill().getName().equals(name));
    }
}
