package de.jinba.server.service;

import de.jinba.server.dto.SkillAddRequest;
import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.AppUserSkill;
import de.jinba.server.entity.Skill;
import de.jinba.server.repository.AppUserRepository;
import de.jinba.server.repository.AppUserSkillRepository;
import de.jinba.server.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final AppUserDetailsService appUserDetailsService;
    private final AppUserRepository appUserRepository;
    private final SkillRepository skillRepository;
    private final AppUserSkillRepository appUserSkillRepository;

    public void removeSkillFromUser(Long id){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to remove skill"));
        appUserSkillRepository.deleteById(id);
        appUserRepository.save(currentUser);
    }

    public void addSkillToUser(SkillAddRequest request) {
        if(!appUserDetailsService.isAuthenticated()){
            throw new IllegalStateException("Unauthenticated user attempted to add skill");
        }
        Optional<Skill> existingSkill = skillRepository.findByName(request.getSkillName());
        if (existingSkill.isPresent()){
            if (hasCurrentUserSkill(request.getSkillName())){
                throw new IllegalStateException("User already has skill");
            }
            AppUserSkill appUserSkill = AppUserSkill.builder()
                    .appUser(appUserDetailsService.getCurrentAuthenticatedUser().get())
                    .level(request.getLevel())
                    .skill(existingSkill.get())
                    .build();
            appUserSkillRepository.save(appUserSkill);
        }else{
            Skill createdSkill = createSkill(request.getSkillName());
            AppUserSkill appUserSkill = AppUserSkill.builder()
                    .appUser(appUserDetailsService.getCurrentAuthenticatedUser().get())
                    .level(request.getLevel())
                    .skill(createdSkill)
                    .build();
            appUserSkillRepository.save(appUserSkill);
        }
    }

    public Skill createSkill(String name){
        Skill skill = Skill.builder()
                .name(name)
                .build();
        return skillRepository.save(skill);
    }

    public Skill getSkillAndCreateIfNotExists(String name){
        Optional<Skill> skill = skillRepository.findByName(name);
        return skill.orElseGet(() -> createSkill(name));
    }


    public boolean hasCurrentUserSkill(String name){
        AppUser currentUser = appUserDetailsService.getCurrentAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated user attempted to add skill"));
        return currentUser.getSkills().stream().anyMatch(skill -> skill.getSkill().getName().equals(name));
    }
}
