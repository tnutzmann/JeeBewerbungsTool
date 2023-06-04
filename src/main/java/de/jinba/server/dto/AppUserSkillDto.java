package de.jinba.server.dto;

import de.jinba.server.entity.AppUserSkill;
import de.jinba.server.entity.enumuration.SkillLevel;

import java.io.Serializable;

public record AppUserSkillDto (Long id, String skillName, SkillLevel skillLevel) implements Serializable {
    public static AppUserSkillDto from(AppUserSkill appUserSkill) {
        return new AppUserSkillDto(
                appUserSkill.getId(),
                appUserSkill.getSkill().getName(),
                appUserSkill.getLevel()
        );
    }
}
